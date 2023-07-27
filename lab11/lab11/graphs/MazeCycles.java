package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int[] parent;
    private final int s = 0;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        parent = new int[m.V()];
        parent[s] = s;
    }

    @Override
    public void solve() {
        Stack<Integer> toVisit = new Stack<>();
        toVisit.push(s);
        while (!toVisit.isEmpty()) {
            int v = toVisit.pop();
            marked[v] = true;
            announce();
            for (int w: maze.adj(v)) {
                if (!marked[w]) {
                    toVisit.push(w);
                    parent[w] = v;
                }
                if (marked[w] && w != parent[v]) {
                    connectCycle(v, w);
                    announce();
                    return;
                }
            }
        }
    }

    private void connectCycle(int end, int start) {
        edgeTo[start] = end;
        int x = end;
        while (x != start) {
            edgeTo[x] = parent[x];
            x = parent[x];
        }
    }

}

