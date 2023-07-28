package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    private int findMinimumUnmarked(int v) {
        int x = s;
        int dist = h(s);
        for (int w: maze.adj(v)) {
            int h = h(w);
            if (!marked[w] && h < dist) {
                x = w;
                dist = h;
            }
        }
        return x;
    }

    /** Performs an A star search from vertex s. */
    private void astar(int x) {
        while (x != t) {
            marked[x] = true;
            announce();
            int w = findMinimumUnmarked(x);
            distTo[w] = distTo[x] + 1;
            edgeTo[w] = x;
            announce();
            x = w;
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

