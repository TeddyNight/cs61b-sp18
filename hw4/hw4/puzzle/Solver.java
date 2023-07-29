package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.HashMap;
import java.util.Map;

public class Solver {
    private Map<WorldState, Integer> distTo;
    private Map<WorldState, Integer> estimate;
    private Stack<WorldState> solution;

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     * @param initial
     */
    public Solver(WorldState initial) {
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
            int curDist = distTo.get(v);
            if (v.isGoal()) {
                buildSolution(cur);
                break;
            }
            for (WorldState w: v.neighbors()) {
                if (parent == null || !w.equals(parent.v)) {
                    // we know already know the fastest way to W
                    int newDist = curDist + 1;
                    if (distTo.containsKey(w) && newDist >= distTo.get(w)) {
                        continue;
                    }
                    distTo.put(w, newDist);
                    toVisit.insert(new Node(w, newDist + estimatedDistanceToGoal(w), cur));
                }
            }
        }
    }

    private void buildSolution(Node cur) {
        while (cur.parent != null) {
            solution.push(cur.v);
            cur = cur.parent;
        }
        solution.push(cur.v);
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     * @return
     */
    public int moves() {
        return solution.size() - 1;
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
