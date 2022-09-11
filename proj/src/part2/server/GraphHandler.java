package part2.server;

import part2.algorithms.GraphAlgorithm;
import part2.component.VertexInMatrixAsGraph;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class GraphHandler implements IHandler{

    private GraphAlgorithm m_GraphAlgorithm;
    private VertexInMatrixAsGraph m_Start, m_End;
    private int[][] m_PrimitiveMatrix;

    public GraphHandler() {
        resetParams();
        m_GraphAlgorithm = new GraphAlgorithm();
    }

    private void resetParams(){
        m_Start = null;
        m_End = null;
        m_PrimitiveMatrix = null;
    }

    @Override
    public void Handle(InputStream i_InClient, OutputStream i_OutClient) throws Exception {

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(i_OutClient);
        ObjectInputStream objectInputStream = new ObjectInputStream(i_InClient);
        boolean dowork = true;

        resetParams();
        while (dowork) {
            switch (objectInputStream.readObject().toString()) {
                case "stop":{
                    dowork = false;
                    break;
                }
                case "FindAllComponents": {
                    m_PrimitiveMatrix = (int[][]) objectInputStream.readObject();
                    objectOutputStream.writeObject(m_GraphAlgorithm.Ex1FindAllComponents(m_PrimitiveMatrix));
                    break;
                }
                case "FindAllShortestRoutes": {
                    m_PrimitiveMatrix = (int[][]) objectInputStream.readObject();
                    m_Start = (VertexInMatrixAsGraph) objectInputStream.readObject();
                    m_End = (VertexInMatrixAsGraph) objectInputStream.readObject();
                    objectOutputStream.writeObject(m_GraphAlgorithm.Ex2FindAllShortestRoutes(m_PrimitiveMatrix,m_Start,m_End));
                    break;
                }
                case "FindNumberOfSubmarines": {
                    m_PrimitiveMatrix = (int[][]) objectInputStream.readObject();
                    objectOutputStream.writeObject(m_GraphAlgorithm.Ex3FindNumberOfTheCorrectSubmarines(m_PrimitiveMatrix));
                    break;
                }
                default:{
                    objectOutputStream.writeObject("The request does not exist");
                }
            }
        }
    }
}



