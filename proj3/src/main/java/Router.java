import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    static class Node implements Comparable<Node> {
        long id;
        double dist;
        Node parent;

        Node(long id, double dist, Node parent) {
            this.id = id;
            this.dist = dist;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(dist, o.dist);
        }
    }
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        List<Long> res = new LinkedList<>();
        long s = g.closest(stlon, stlat);
        long t = g.closest(destlon, destlat);
        PriorityQueue<Node> toVisit = new PriorityQueue<>();
        Map<Long, Double> distTo = new HashMap<>();
        distTo.put(s, 0d);
        toVisit.add(new Node(s, g.distance(s, t), null));
        while (!toVisit.isEmpty()) {
            Node cur = toVisit.poll();
            long v = cur.id;
            if (v == t) {
                buildPath(cur, res);
                break;
            }
            double curDist = distTo.get(v);
            for (Long w: g.adjacent(v)) {
                if (cur.parent != null && w.equals(cur.parent.id)) {
                    continue;
                }
                // add actual dist instead of 1
                double newDist = curDist + g.distance(v, w);
                if (distTo.containsKey(w) && Double.compare(newDist, distTo.get(w)) >= 0) {
                    continue;
                }
                distTo.put(w, newDist);
                toVisit.add(new Node(w, newDist + g.distance(w, t), cur));
            }
        }
        return res;
    }

    private static void buildPath(Node cur, List<Long> res) {
        if (cur == null) {
            return;
        }
        buildPath(cur.parent, res);
        res.add(cur.id);
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> nav = new LinkedList<>();
        NavigationDirection lastNav = new NavigationDirection();
        double lastBearing = g.bearing(route.get(0), route.get(1));
        lastNav.way = getWay(g, route.get(0), route.get(1));
        lastNav.direction = NavigationDirection.START;
        lastNav.distance = g.distance(route.get(0), route.get(1));
        nav.add(lastNav);
        for (int i = 2; i < route.size(); i++) {
            long lastNode = route.get(i - 1);
            long node = route.get(i);
            String way = getWay(g, lastNode, node);
            if (way.equals(lastNav.way)) {
                lastBearing = g.bearing(lastNode, node);
                lastNav.distance += g.distance(lastNode, node);
            } else {
                double curBearing = g.bearing(lastNode, node);
                lastNav = new NavigationDirection();
                lastNav.direction = getDirect(lastBearing, curBearing);
                lastNav.way = way;
                lastNav.distance = g.distance(lastNode, node);
                nav.add(lastNav);
                lastBearing = curBearing;
            }
        }
        return nav;
    }

    private static String getWay(GraphDB g, long v, long w) {
        String ret = g.getWay(v, w);
        if (ret == null) {
            ret = NavigationDirection.UNKNOWN_ROAD;
        }
        return ret;
    }

    private static int getDirect(double lastBearing, double curBearing) {
        int direction = NavigationDirection.STRAIGHT;
        double angle = curBearing - lastBearing;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }
        if (angle >= -30 && angle < -15) {
            direction = NavigationDirection.SLIGHT_LEFT;
        } else if (angle <= 30 && angle > 15) {
            direction = NavigationDirection.SLIGHT_RIGHT;
        } else if (angle >= -100 && angle < -30) {
            direction = NavigationDirection.LEFT;
        } else if (angle <= 100 && angle > 30) {
            direction = NavigationDirection.RIGHT;
        } else if (angle < -100) {
            direction = NavigationDirection.SHARP_LEFT;
        } else if (angle > 100) {
            direction = NavigationDirection.SHARP_RIGHT;
        }
        return direction;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
//        public static final String UNKNOWN_ROAD = "unknown road";
        public static final String UNKNOWN_ROAD = "";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
