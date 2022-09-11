package part2.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixAsGraph implements Serializable,IGraph<VertexInMatrixAsGraph> {

    private int[][] m_PrimitiveMatrix;

    public MatrixAsGraph(int[][] i_Array){
        m_PrimitiveMatrix = Arrays.stream(i_Array).map(row -> row.clone()).toArray(value -> new int[value][]);
    }

    public final int[][] getPrimitiveMatrix() {
        return m_PrimitiveMatrix;
    }

    public int GetMatrixAsGraphSize(){

        return m_PrimitiveMatrix.length * m_PrimitiveMatrix[0].length;
    }

    public void SetValue(final VertexInMatrixAsGraph i_Vertex, final int i_Value) {
        m_PrimitiveMatrix[i_Vertex.GetRow()][i_Vertex.GetColumn()] = i_Value;
    }

    public int GetValue(VertexInMatrixAsGraph i_Vertex) {
        return m_PrimitiveMatrix[i_Vertex.GetRow()][i_Vertex.GetColumn()];
    }

    public void PrintMatrixAsGraph(){

        for (int[] row : m_PrimitiveMatrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    @Override
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        for (int[] row : m_PrimitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public Collection<VertexInMatrixAsGraph> GetAllAdjacentIndices(final VertexInMatrixAsGraph i_Vertex){

        Collection<VertexInMatrixAsGraph> finalListOfVertex = new ArrayList<>();

        finalListOfVertex.addAll(getAdjacentIndicesNotDiagonal(i_Vertex));
        finalListOfVertex.addAll(getAdjacentIndicesInDiagonal(i_Vertex));

        return finalListOfVertex;
    }


    private Collection<VertexInMatrixAsGraph> getAdjacentIndicesInDiagonal(final VertexInMatrixAsGraph i_Vertex) {

        Collection<VertexInMatrixAsGraph> finalListOfVertexInDiagonal = new ArrayList<>();
        int extracted = -1;

        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow() -1][i_Vertex.GetColumn() - 1];
            finalListOfVertexInDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow() - 1, i_Vertex.GetColumn() - 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow() -1][i_Vertex.GetColumn() + 1];
            finalListOfVertexInDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow() - 1, i_Vertex.GetColumn() + 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow() + 1][i_Vertex.GetColumn() - 1];
            finalListOfVertexInDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow() + 1, i_Vertex.GetColumn() - 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow() + 1][i_Vertex.GetColumn() + 1];
            finalListOfVertexInDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow() + 1, i_Vertex.GetColumn() + 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return finalListOfVertexInDiagonal;
    }

    private Collection<VertexInMatrixAsGraph> getAdjacentIndicesNotDiagonal(final VertexInMatrixAsGraph i_Vertex) {

        Collection<VertexInMatrixAsGraph> finalListOfVertexNotDiagonal = new ArrayList<>();
        int extracted = -1;

        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow() + 1][i_Vertex.GetColumn()];
            finalListOfVertexNotDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow() + 1, i_Vertex.GetColumn()));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow()][i_Vertex.GetColumn() + 1];
            finalListOfVertexNotDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow(), i_Vertex.GetColumn() + 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow() - 1][i_Vertex.GetColumn()];
            finalListOfVertexNotDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow() - 1, i_Vertex.GetColumn()));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = m_PrimitiveMatrix[i_Vertex.GetRow()][i_Vertex.GetColumn() - 1];
            finalListOfVertexNotDiagonal.add(new VertexInMatrixAsGraph(i_Vertex.GetRow(), i_Vertex.GetColumn() - 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return finalListOfVertexNotDiagonal;
    }

    public Collection<VertexInMatrixAsGraph> GetAllReachables(VertexInMatrixAsGraph i_Vertex) {

        ArrayList<VertexInMatrixAsGraph> filteredIndices = new ArrayList<>();

        this.GetAllAdjacentIndices(i_Vertex).stream().filter(i-> GetValue(i) == 1)
                .map(neighbor->filteredIndices.add(neighbor)).collect(Collectors.toList());

        fillTheFathersName(i_Vertex, filteredIndices);

        return filteredIndices;
    }

    @Override
    public Collection<VertexInMatrixAsGraph> GetAllVertex() {

        List<VertexInMatrixAsGraph> finalList = new ArrayList<>();

        for (int i = 0; i < m_PrimitiveMatrix.length; i++){
            for(int j = 0; j < m_PrimitiveMatrix[0].length ; j++){

                if(m_PrimitiveMatrix[i][j] == 1){
                    finalList.add(new VertexInMatrixAsGraph(i,j));
                }
            }
        }
        return finalList;
    }


    private void fillTheFathersName(VertexInMatrixAsGraph i_Vertex, ArrayList<VertexInMatrixAsGraph> i_FilteredIndices) {

        for (VertexInMatrixAsGraph i : i_FilteredIndices) {
            if(i.GetParentName() == null)
                i.SetParentName(i_Vertex.toString());
        }
    }

//
//    public Collection<VertexInMatrixAsGraph> GetReachablesNotInDiagonal(VertexInMatrixAsGraph i_Vertex) {
//
//        ArrayList<VertexInMatrixAsGraph> filteredIndices = new ArrayList<>();
//
//        this.GetAdjacentIndicesNotDiagonal(i_Vertex).stream().filter(i -> GetValue(i) == 1)
//                .map(neighbor -> filteredIndices.add(neighbor)).collect(Collectors.toList());
//
//        FillTheFathersName(i_Vertex, filteredIndices);
//
//        return filteredIndices;
//    }


}