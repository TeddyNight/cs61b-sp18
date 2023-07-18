package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        MapGenerator mg = null;
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        welcomeFrame();
        char choice = typeChoice();
        while (mg == null) {
            switch (choice) {
                case 'n':
                    seedFrame();
                    long seed = typeSeed();
                    mg = new MapGenerator(seed, WIDTH, HEIGHT - 2);
                    break;
                case 'l':
                    mg = loadWorld();
                    break;
                case 'q':
                    return;
                default:
                    choice = typeChoice();
            }
        }
        TETile[][] finalWorldFrame = mg.getMap();
        Player player = mg.getPlayer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        gameFrame(finalWorldFrame, player);
        saveWorld(mg);
    }

    private void gameFrame(TETile[][] finalWorldFrame, Player player) {
        while (true) {
            ter.renderFrame(finalWorldFrame);
            renderHeadsup(finalWorldFrame);
            if (StdDraw.hasNextKeyTyped()) {
                char in = StdDraw.nextKeyTyped();
                if (in == ':') {
                    continue;
                }
                if (in == 'q') {
                    break;
                }
                player.move(in);
            }
            if (StdDraw.isMousePressed()) {
                moveMouse(finalWorldFrame, player);
            }
        }
    }

    private void moveMouse(TETile[][] finalWorldFrame, Player player) {

    }

    private void renderHeadsup(TETile[][] finalWorldFrame) {
        StdDraw.setPenColor(Color.white);
        String msg = "Nothing";
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (y < HEIGHT - 2) {
            TETile tetile = finalWorldFrame[x][y];
            if (tetile.equals(Tileset.WALL)) {
                msg = "Wall";
            } else if (tetile.equals(Tileset.FLOOR)) {
                msg = "Floor";
            } else if (tetile.equals(Tileset.PLAYER)) {
                msg = "Player";
            }
        }
        StdDraw.textLeft(0, HEIGHT - 1, msg);
        StdDraw.show();
        StdDraw.pause(25);
    }

    private char typeChoice() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return StdDraw.nextKeyTyped();
            }
        }
    }

    private long typeSeed() {
        long seed = 0;
        char c = '\0';
        do {
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                if (c >= '0' && c <= '9') {
                    seed = (c - '0') + seed * 10;
                }
            }
        } while (c != 's');
        return seed;
    }

    private void welcomeFrame() {
        final String[] opts = {"New Game (N)", "Load Game (L)", "Quit (Q)"};
        Font font = new Font("Arial", Font.BOLD, 48);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "CS61B: THE GAME");
        showOption(opts);
        StdDraw.show();
    }


    private void showOption(String[] opts) {
        Font font = new Font("Arial", Font.BOLD, 24);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        for (int i = 0; i < opts.length; i++) {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - i * 2, opts[i]);
        }
    }

    private void seedFrame() {
        Font font = new Font("Arial", Font.BOLD, 32);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Input the seed, ends with S");
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        TETile[][] finalWorldFrame = null;
        MapGenerator mg = null;
        int i = 1;
        switch (input.charAt(0)) {
            case 'n':
                char[] chars = input.toCharArray();
                long seed = 0;
                for (; i < input.length() && chars[i] >= '0' && chars[i] <= '9'; i++) {
                    seed = seed * 10 + (chars[i] - '0');
                }
                mg = new MapGenerator(seed, WIDTH, HEIGHT - 2);
                break;
            case 'l':
                mg = loadWorld();
                break;
            default:
                return null;
        }
        if (mg != null) {
            finalWorldFrame = mg.getMap();
            Player player = mg.getPlayer();
            if (i < input.length()) {
                player.moveWithString(input.substring(i));
            }
        }
        saveWorld(mg);
        return finalWorldFrame;
    }

    private void saveWorld(MapGenerator world) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./Game.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(world);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MapGenerator loadWorld() {
        MapGenerator mg = null;
        try {
            FileInputStream fileIn = new FileInputStream("./Game.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mg = (MapGenerator) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mg;
    }
}
