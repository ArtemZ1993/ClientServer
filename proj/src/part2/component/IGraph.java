package part2.component;

import java.util.Collection;

public interface IGraph<T> {

    public Collection<T> GetAllAdjacentIndices(final T i_Vertex);
    public Collection<T> GetAllReachables(final T i_Vertex);
    public Collection<T> GetAllVertex();

    }
