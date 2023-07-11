package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private Position pos;
    private int width;
    private int height;
    private TETile[][] map;

    public Player(Position pos, int width, int height, TETile[][] map) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.map = map;
        draw();
    }

    private void draw() {
        map[pos.getX()][pos.getY()] = Tileset.PLAYER;
    }

    private void clear() {
        map[pos.getX()][pos.getY()] = Tileset.FLOOR;
    }

    private boolean isCollision(Position newP) {
        /** use "==" will compare base on their address cause problems
         *  because every time the WALL's address is different
         */
        return map[newP.getX()][newP.getY()].equals(Tileset.WALL);
    }

    private boolean isValidPos(Position newP) {
        return (newP.getY() < height) && (newP.getX() < width)
                && (newP.getX() >= 0) && (newP.getY() >= 0);
    }

    public void move(Position newP) {
        if (!isValidPos(newP) || isCollision(newP)) {
            return;
        }
        clear();
        pos = newP;
        draw();
    }

    public void move(char direction) {
        switch (direction) {
            case 'a':
                move(new Position(pos.getX() - 1, pos.getY()));
                break;
            case 'd':
                move(new Position(pos.getX() - 1, pos.getY()));
                break;
            case 'w':
                move(new Position(pos.getX(), pos.getY() + 1));
                break;
            case 's':
                move(new Position(pos.getX(), pos.getY() - 1));
                break;
            default:
                break;
        }
    }

    public void moveWithString(String inputs) {
        for (char c: inputs.toCharArray()) {
            move(c);
        }
    }

}
