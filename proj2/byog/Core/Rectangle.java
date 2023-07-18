package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;

public class Rectangle {
    protected Position leftDown;
    protected Position rightUp;
    public void drawEdge(TETile[][] map) {
        drawHorizontally(map, leftDown.getY());
        drawHorizontally(map, rightUp.getY());
        drawVertically(map, leftDown.getX());
        drawVertically(map, rightUp.getX());
    }

    public void drawFloor(TETile[][] map) {
        for (int i = leftDown.getX() + 1; i < rightUp.getX(); i++) {
            for (int j = leftDown.getY() + 1; j < rightUp.getY(); j++) {
                map[i][j] = Tileset.FLOOR;
            }
        }
    }

    protected void drawVertically(TETile[][] map, int x) {
        int min = leftDown.getY();
        int max = rightUp.getY();
        for (int i = min; i <= max; i++) {
            map[x][i] = Tileset.WALL;
        }
    }

    protected void drawHorizontally(TETile[][] map, int y) {
        int min = leftDown.getX();
        int max = rightUp.getX();
        for (int i = min; i <= max; i++) {
            map[i][y] = Tileset.WALL;
        }
    }

    public Position getLeftDown() {
        return leftDown;
    }

    public Position getRightUp() {
        return rightUp;
    }

    public void setLeftDown(Position leftDown) {
        this.leftDown = leftDown;
    }

    public void setRightUp(Position rightUp) {
        this.rightUp = rightUp;
    }

    public boolean overlaps(List<Room> rooms) {
        for (Room room: rooms) {
            if (overlaps(room)) {
                return true;
            }
        }
        return false;
    }

    public boolean overlaps(Room r) {
        if (r.leftDown.equals(this.rightUp) || r.rightUp.equals(this.leftDown)) {
            return false;
        }
        // left neighbor
        if (r.rightUp.getX() <= leftDown.getX()) {
            return false;
        }
        // right neighbor
        if (r.leftDown.getX() >= rightUp.getX()) {
            return false;
        }
        // up neighbor
        if (r.leftDown.getY() >= rightUp.getY()) {
            return false;
        }

        if (r.rightUp.getY() <= leftDown.getY()) {
            return false;
        }

        return true;
    }
}
