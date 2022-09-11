package part2.component;

import java.io.Serializable;
import java.util.Objects;

public class VertexInMatrixAsGraph implements Serializable, IVertex {

    private int m_Column;
    private int m_Row;
    private String m_ParentName = null;

    // Constructor
    public VertexInMatrixAsGraph(int i_Row, int i_Column) {
        this.m_Row = i_Row;
        this.m_Column = i_Column;
    }

    public int GetRow() {
        return m_Row;
    }

    public int GetColumn() {
        return m_Column;
    }

    @Override
    public String GetParentName() {
        return m_ParentName;
    }

    @Override
    public void SetParentName(String i_ParentName) {
        this.m_ParentName = i_ParentName;
    }

    @Override
    public String toString() {
        return "(" + m_Row + "," + m_Column + ")";
    }

    @Override
    public boolean equals(Object i_Object) {
        if (this == i_Object) return true;
        if (i_Object == null || getClass() != i_Object.getClass()) return false;
        VertexInMatrixAsGraph vertex = (VertexInMatrixAsGraph) i_Object;
        return m_Row == vertex.m_Row &&
                m_Column == vertex.m_Column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_Row, m_Column);
    }
}