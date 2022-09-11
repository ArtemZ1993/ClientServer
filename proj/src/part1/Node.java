package part1;

import java.util.Collection;
import java.util.List;

public interface Node {
    public <T extends HasUUID> Collection<Node> getCollection(Class<T> o);

}
