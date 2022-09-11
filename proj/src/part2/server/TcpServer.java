package part2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a multi-threaded server
 */
public class TcpServer {
    private final int m_Port;
    private AtomicBoolean m_StopServer;
    private ThreadPoolExecutor m_Executor;
    private IHandler m_RequestConcreteIHandler;

    public TcpServer(int i_Port) {
        m_Port = i_Port;
        m_StopServer =  new AtomicBoolean(false);
        m_Executor = null;
    }

    public void RunServer(IHandler i_ConcreteIHandler) {

        this.m_RequestConcreteIHandler = i_ConcreteIHandler;

        Runnable mainLogic = () -> {
            try {
                m_Executor = new ThreadPoolExecutor(3, 5, 10,TimeUnit.SECONDS, new PriorityBlockingQueue<>());

                ServerSocket server = new ServerSocket(m_Port);
                server.setSoTimeout(1000);

                while (!m_StopServer.get()) {
                    try {

                        Socket request = server.accept();

                        Runnable runnable = () -> {
                            try {
                                m_RequestConcreteIHandler.Handle(request.getInputStream(),request.getOutputStream());
                                // Close all streams
                                request.getInputStream().close();
                                request.getOutputStream().close();
                                request.close();
                            } catch (Exception e) {
                                System.out.println("server::"+e.getMessage());
                                System.err.println(e.getMessage());
                            }
                        };

                        m_Executor.execute(runnable);

                    } catch (SocketTimeoutException ignored) { }
                }
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(mainLogic).start();

    }

    public void stop() {
        if (!m_StopServer.get()) {
            m_StopServer.getAndSet(true);
            if (m_Executor != null) {
                m_Executor.shutdown();
            }
        }
    }

    public static void main(String[] args) {

        TcpServer tcpServer = new TcpServer(8010);
        tcpServer.RunServer(new GraphHandler());

    }
}