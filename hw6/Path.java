import java.util.HashSet;
import java.util.Set;

public class Path {
    private Set<Position> marked;
    private Position lastPos;

    Path(Position p) {
        marked = new HashSet<>();
        marked.add(p);
        lastPos = p;
    }

    Path(Path p) {
        marked = new HashSet<>(p.marked);
        lastPos = p.lastPos;
    }

    boolean hasVisited(Position p) {
        return marked.contains(p);
    }

    Path appendPos(Position p) {
        lastPos = p;
        marked.add(p);
        return this;
    }

    Position getLastPos() {
        return lastPos;
    }

    @Override
    public String toString() {
        return marked.toString();
    }
}
