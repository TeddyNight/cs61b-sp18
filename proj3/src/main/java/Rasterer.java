import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        if (!validParams(params)) {
            results.put("query_success", false);
            return results;
        }
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double w = params.get("w");
        double h = params.get("h");
        double boxLonDPP = calcLonDPP(lrlon, ullon, w);
        int depth = getDepth(boxLonDPP, w, h);
        results.put("depth", depth);
        return results;
    }

    private int getDepth(double boxLonDPP, double w, double h) {
        int depth = 0;
        double lrlon = MapServer.ROOT_LRLON;
        double ullon = MapServer.ROOT_ULLON;
        double lonDPP = calcLonDPP(lrlon, ullon, MapServer.TILE_SIZE);
        while (lonDPP > boxLonDPP) {
            depth++;
            ullon = lrlon + (ullon - lrlon) / 2;
            lonDPP = calcLonDPP(lrlon, ullon, MapServer.TILE_SIZE);
        }
        return depth;
    }

    private double calcLonDPP(double lrlon, double ullon, double w) {
        return (lrlon - ullon) / w;
    }

    private boolean validParams(Map<String, Double> params) {
        if (!params.containsKey("lrlon") || !params.containsKey("ullon")
                || !params.containsKey("w") || !params.containsKey("h")
                || !params.containsKey("ullat") || !params.containsKey("lrlat")) {
            return false;
        }
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        if (ullon > lrlon) {
            return false;
        }
        if (ullat > lrlat) {
            return false;
        }
        if (ullon < MapServer.ROOT_ULLON) {
            return false;
        }
        if (lrlon > MapServer.ROOT_LRLON) {
            return false;
        }
        if (lrlat < MapServer.ROOT_LRLAT) {
            return false;
        }
        if (ullat > MapServer.ROOT_ULLAT) {
            return false;
        }
        return true;
    }

}
