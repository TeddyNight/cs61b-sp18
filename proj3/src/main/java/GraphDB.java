import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    static class Node {
        long id;
        double lat;
        double lon;
        String name;
        Set<Long> ways;
        Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.ways = new HashSet<>();
        }
    }
    static class Edge {
        long id;
        String name;
        String maxspeed;
        List<Long> nodes;
        Edge(long id) {
            this.id = id;
            this.nodes = new ArrayList<>();
        }
    }
    private Map<Long, Node> nodes = new HashMap<>();
    private Map<Long, Edge> edges = new HashMap<>();
    private Map<Long, Set<Long>> graph = new HashMap<>();
    private Map<String, List<Long>> locations = new HashMap<>();
    private Trie locationNames = new Trie();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        List<Long> orphan = new LinkedList<>();
        for (Long id: nodes.keySet()) {
            Node node = nodes.get(id);
            if (graph.get(id).isEmpty()) {
                orphan.add(id);
            }
            if (node.name != null) {
                if (!locations.containsKey(node.name)) {
                    locations.put(node.name, new LinkedList<>());
                }
                locations.get(node.name).add(node.id);
                locationNames.insert(node.name);
            }
        }
        for (Long node: orphan) {
//            nodes.remove(node);
            graph.remove(node);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return graph.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long v = 0;
        double minDist = Double.MAX_VALUE;
        for (long w: graph.keySet()) {
            Node node = nodes.get(w);
            double dist = distance(lon, lat, node.lon, node.lat);
            if (dist <= minDist) {
                v = node.id;
                minDist = dist;
            }
        }
        return v;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    void addNode(Node node) {
        nodes.put(node.id, node);
        graph.put(node.id, new HashSet<>());
    }

    void addEdge(Edge edge) {
        edges.put(edge.id, edge);
        List<Long> list = edge.nodes;
        nodes.get(list.get(0)).ways.add(edge.id);
        for (int i = 1; i < list.size(); i++) {
            nodes.get(list.get(i)).ways.add(edge.id);
            graph.get(list.get(i - 1)).add(list.get(i));
            graph.get(list.get(i)).add(list.get(i - 1));
        }
    }

    /**
     * find the way that contains specific two nodes
     * @param v
     * @param w
     * @return the id of way
     */
    String getWay(long v, long w) {
        Set<Long> ways = nodes.get(v).ways;
        for (Long way: ways) {
            if (edges.get(way).nodes.contains(w)) {
                return edges.get(way).name;
            }
        }
        throw new RuntimeException();
    }

    List<String> getLocationsByPrefix(String prefix) {
        return locationNames.findPrefix(prefix);
    }

    Map<String, Object> getLocationById(Long id) {
        Map<String, Object> res = new HashMap<>();
        GraphDB.Node location = nodes.get(id);
        res.put("id", location.id);
        res.put("name", location.name);
        res.put("lat", location.lat);
        res.put("lon", location.lon);
        return res;
    }

    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> res = new LinkedList<>();
        for (String name: getLocationsByPrefix(locationName)) {
            for (Long id: locations.get(name)) {
                res.add(getLocationById(id));
            }
        }
        return res;
    }
}
