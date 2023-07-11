package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
//        String dir = this.getClass().getResource("/").getFile();
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
                mg = new MapGenerator(seed, WIDTH, HEIGHT);
                finalWorldFrame = mg.getMap();
                Player player = mg.getPlayer();
                if (i < input.length()) {
                    player.moveWithString(input.substring(i));
                }
                break;
            case 'l':
                mg = loadWorld();
                if (mg != null) {
                    finalWorldFrame = mg.getMap();
                    player = mg.getPlayer();
                    if (i < input.length()) {
                        player.moveWithString(input.substring(i));
                    }
                }
                break;
            default:
                break;
        }
//        saveWorld(mg);
        return finalWorldFrame;
    }

    private void saveWorld(MapGenerator world) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./Game.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(world);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MapGenerator loadWorld() {
        MapGenerator mg = null;
        try {
            FileInputStream fileIn = new FileInputStream("./Game.ser");
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
