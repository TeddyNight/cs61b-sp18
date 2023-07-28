package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

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

    /** Performs an A star search from vertex s. */
    private void astar(int x) {
        MinPQ<Node> toVisit = new MinPQ<Node>();
        toVisit.insert(new Node(x, distTo[x] + h(x)));
        marked[x] = true;
        while (!toVisit.isEmpty() && !targetFound) {
            int v = toVisit.delMin().v;
            announce();
            if (v == t) {
                targetFound = true;
            }
            for (int w: maze.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    toVisit.insert(new Node(w, distTo[w] + h(w)));
                    edgeTo[w] = v;
                    marked[v] = true;
                    announce();
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

    private class Node implements Comparable<Node> {
        int v;
        int dist;

        @Override
        public int compareTo(Node o) {
            return dist - o.dist;
        }

        Node(int v, int dist) {
            this.v = v;
            this.dist = dist;
        }
    }

}

