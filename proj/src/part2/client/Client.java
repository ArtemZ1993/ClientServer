package part2.client;

import part2.component.VertexInMatrixAsGraph;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;

public class Client {

    public void ProgramTest() throws IOException, ClassNotFoundException {
        // In order to request something over TCP from a server, we need a port number and an IP address
        Socket socket = new Socket("127.0.0.1", 8010);
        // socket is an abstraction of 2-way data pipe
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        // use decorators
        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);

        int[][] testForEx1 = {

                {1, 0, 1, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 0, 1},

        };

        int[][] testForEx2 = {

                {1, 1, 0, 1, 0, 0},
                {0, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 0},
        };

        int[][] testForEx3 = {

                {1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, 0},
                {1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 1, 1},
                {0, 0, 0, 1, 0, 1},
                {1, 1, 0, 1, 0, 1},
                {0, 1, 0, 1, 1, 1},
        };


        testEX1(fromServer, toServer, testForEx1);

        testEX2(fromServer, toServer, testForEx2);

        testEX3(fromServer, toServer, testForEx3);

        stopStream(socket, fromServer, toServer);
    }

    private void stopStream(Socket socket, ObjectInputStream fromServer, ObjectOutputStream toServer) throws IOException {

        System.out.println();
        toServer.writeObject("stop");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("Finish\nAll streams are closed");
    }

    private void testEX3(ObjectInputStream fromServer, ObjectOutputStream toServer, int[][] testForEx3) throws IOException, ClassNotFoundException {

        System.out.println("\nEx3 Find Number Of Submarines\n");
        try {
            toServer.writeObject("FindNumberOfSubmarines");
            // according to protocol, after "matrix" string, send 2d int array
            toServer.writeObject(testForEx3);

            System.out.println(String.format("The number of correct submarines in the matrix is: %s",(int) fromServer.readObject()));
        }
        catch (Exception e){
            System.out.println(fromServer.readObject().toString());
        }
    }

    private void testEX2(ObjectInputStream fromServer, ObjectOutputStream toServer, int[][] testForEx2) throws IOException, ClassNotFoundException {

        System.out.println("\nEx2 Find All Shortest Routes\n");
        try {
            toServer.writeObject("FindAllShortestRoutes");
            // according to protocol, after "matrix" string, send 2d int array
            toServer.writeObject(testForEx2);
            toServer.writeObject(new VertexInMatrixAsGraph(0,0));
            toServer.writeObject(new VertexInMatrixAsGraph(2,4));

            var allShortRoutes = (HashSet<Collection<VertexInMatrixAsGraph>>) fromServer.readObject();

            System.out.println(String.format("Number of routes is: %s\n" +
                    "Length of the short route is: %s\n" +
                    "All routes:",allShortRoutes.size(),allShortRoutes.iterator().next().size()));
            for (Collection<VertexInMatrixAsGraph> ShortRoute : allShortRoutes) {
                System.out.println(ShortRoute);
            }
        }
        catch (Exception e){
            System.out.println(fromServer.readObject().toString());
        }
    }

    private void testEX1(ObjectInputStream fromServer, ObjectOutputStream toServer, int[][] testForEx1) throws IOException, ClassNotFoundException {

        System.out.println("\nEx1 Find All Components\n");
        try {
            toServer.writeObject("FindAllComponents");
            // according to protocol, after "matrix" string, send 2d int array
            toServer.writeObject(testForEx1);

            var allComponents = (Collection<HashSet<VertexInMatrixAsGraph>>) fromServer.readObject();

            System.out.println(String.format("Number of components is: %s\n" +
                    "All components:",allComponents.size()));
            for (var component : allComponents) {
                System.out.println(component);
            }
        }
        catch (Exception e){
            System.out.println(fromServer.readObject().toString());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Client client = new Client();
        client.ProgramTest();
    }
}

