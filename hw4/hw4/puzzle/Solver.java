package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.HashMap;
import java.util.Map;

public class Solver {
    Map<WorldState, Integer> distTo;
    Map<WorldState, WorldState> edgeTo;
    WorldState s;
    WorldState t;
    Map<WorldState, Integer> estimate;

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     * @param initial
     */
    public Solver(WorldState initial) {
        s = initial;
        t = null;
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        estimate = new HashMap<>();
        edgeTo.put(initial, null);
        distTo.put(initial, 0);
        aStar(initial);
    }

    private int estimatedDistanceToGoal(WorldState w) {
        if (!estimate.containsKey(w)) {
            estimate.put(w, w.estimatedDistanceToGoal());
        }
        return estimate.get(w);
    }

    private void aStar(WorldState initial) {
        MinPQ<Node> toVisit = new MinPQ<>();
        toVisit.insert(new Node(initial, estimatedDistanceToGoal(initial), null));
        while (!toVisit.isEmpty()) {
            Node cur = toVisit.delMin();
            WorldState v = cur.v;
            WorldState parent = cur.parent;
            if (v.isGoal()) {
                t = v;
                break;
            }
            for (WorldState w: v.neighbors()) {
                if (!w.equals(parent)) {
                    // we know already know the fastest way to W
                    if (distTo.containsKey(w) && distTo.get(v) + 1 > distTo.get(w)) {
                        continue;
                    }
                    edgeTo.put(w, v);
                    distTo.put(w, distTo.get(v) + 1);
                    toVisit.insert(new Node(w, distTo.get(w) + estimatedDistanceToGoal(w), v));
                }
            }
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     * @return
     */
    public int moves() {
        return distTo.get(t);
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     * @return
     */
    public Iterable<WorldState> solution() {
        Stack<WorldState> res = new Stack<>();
        WorldState v = t;
        while (v != null) {
            res.push(v);
            v = edgeTo.get(v);
        }
        res.push(s);
        return res;
    }

    private class Node implements Comparable<Node> {
        WorldState v;
        int dist;
        WorldState parent;

        Node(WorldState v, int dist, WorldState parent) {
            this.v = v;
            this.dist = dist;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node o) {
            return dist - o.dist;
        }
    }
}
