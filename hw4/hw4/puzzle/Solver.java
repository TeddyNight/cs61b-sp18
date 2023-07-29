package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.HashMap;
import java.util.Map;

public class Solver {
    Map<WorldState, Integer> distTo;
    WorldState s;
    WorldState t;
    Map<WorldState, Integer> estimate;
    Stack<WorldState> solution;

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
        distTo = new HashMap<>();
        estimate = new HashMap<>();
        solution = new Stack<>();
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
            Node parent = cur.parent;
            WorldState v = cur.v;
            if (v.isGoal()) {
                buildSolution(cur);
                t = v;
                break;
            }
            for (WorldState w: v.neighbors()) {
                if (parent == null || !w.equals(parent.v)) {
                    // we know already know the fastest way to W
                    if (distTo.containsKey(w) && distTo.get(v) + 1 > distTo.get(w)) {
                        continue;
                    }
                    distTo.put(w, distTo.get(v) + 1);
                    toVisit.insert(new Node(w, distTo.get(w) + estimatedDistanceToGoal(w), cur));
                }
            }
        }
    }

    private void buildSolution(Node cur) {
        while (cur.parent != null) {
            solution.push(cur.v);
            cur = cur.parent;
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
        return solution;
    }

    private class Node implements Comparable<Node> {
        WorldState v;
        int dist;
        Node parent;

        Node(WorldState v, int dist, Node parent) {
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
