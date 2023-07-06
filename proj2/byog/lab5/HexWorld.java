package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    /**
     * given the position of the most down and left TETile
     * @param tiles
     * @param s
     * @param x
     * @param y
     */
    public static void addHexagon(TETile[][] tiles, int s, int x, int y) {
        TETile tile = randomTile();
        for (int i = 0; i < s + s; i++) {
            int begin = posOfBegin(i, s, x);
            int end = posOfEnd(i, s, x);
            for (int j = begin; j < end; j++) {
                tiles[j][y + i] = tile;
            }
        }
    }

    public static int posOfBegin(int curLine, int s, int x) {
        if (curLine < s) {
            return x - curLine;
        }
        return x - s + 1 + (curLine - s);
    }

    public static int posOfEnd(int curLine, int s, int x) {
        if (curLine < s) {
            return x + s + curLine;
        }
        return x + s + s - 1 - (curLine - s);
    }

    private static TETile randomTile() {
        int tileNum = new Random().nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.MOUNTAIN;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.TREE;
            default: return Tileset.SAND;
        }
    }

    /**
     * give the position of the lowest hexagon
     * @param tiles
     * @param s
     * @param num
     * @param x
     * @param y
     */
    private static void columnHexagon(TETile[][] tiles, int s, int num, int x, int y) {
        for (int i = 0; i < num; i++) {
            addHexagon(tiles, s, x, y);
            y = y + s + s;
        }
    }

    public static void tesselateHexagon(TETile[][] tiles, int s, int x, int y) {
        int offset = s * 2;
        columnHexagon(tiles, s, 3, x - (offset - 1) * 2, y + s * 2);
        columnHexagon(tiles, s, 4, x - (offset - 1), y + s);
        columnHexagon(tiles, s, 5, x, y);
        columnHexagon(tiles, s, 4, x + (offset - 1), y + s);
        columnHexagon(tiles, s, 3, x + (offset - 1) * 2, y + s * 2);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        tesselateHexagon(tiles, 5, 23, 0);

        ter.renderFrame(tiles);
    }
}
