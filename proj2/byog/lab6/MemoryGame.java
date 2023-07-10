package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < n; i++) {
            res.append(CHARACTERS[rand.nextInt(26)]);
        }
        return res.toString();
    }

    public void drawFrame(String s) {
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, s);
        if (!gameOver) {
            StdDraw.textLeft(0, height - 1, "Round:" + round);
            StdDraw.text(width / 2, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[rand.nextInt(7)]);
            StdDraw.line(0, height - 2, width, height - 2);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (char letter: letters.toCharArray()) {
            drawFrame(String.valueOf(letter));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            StdDraw.clear(Color.black);
            StdDraw.show();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < n; i++) {
            while (!StdDraw.hasNextKeyTyped()) {
            }
            res.append(StdDraw.nextKeyTyped());
            drawFrame(res.toString());
        }
        return res.toString();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        this.round = 1;
        this.gameOver = false;
        //TODO: Establish Game loop
        while (!this.gameOver) {
            this.playerTurn = false;
            drawFrame("Round:" + this.round);
            String answer = generateRandomString(this.round);
            flashSequence(answer);
            this.playerTurn = true;
            drawFrame("");
            String inputStr = solicitNCharsInput(this.round);
            if (inputStr.equals(answer)) {
                round++;
            } else {
                this.gameOver = true;
            }
        }
        drawFrame("Game Over! You made it to round:" + this.round);
    }

}
