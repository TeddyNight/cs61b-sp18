package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class MapVisualTest {
    private static final int SEED = 114514;
    private static final int WIDTH = 90;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        MapGenerator map = new MapGenerator(SEED, WIDTH, HEIGHT);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] tiles = map.getMap();
        ter.renderFrame(tiles);
    }
}
