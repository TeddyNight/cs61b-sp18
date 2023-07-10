package byog.Core;

import byog.TileEngine.TETile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString(args[0]);
            System.out.println(TETile.toString(worldState));
            // TODO do saving works
            try {
                FileOutputStream fileOut = new FileOutputStream("Game.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(game.mg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
