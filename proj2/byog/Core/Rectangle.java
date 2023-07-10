package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;

public class Rectangle {
    protected Position leftDown;
    protected Position rightUp;
    public void drawEdge(TETile[][] map) {
        drawHorizontally(map);
        drawVertically(map);
    }

    public void drawFloor(TETile[][] map) {
        for (int i = leftDown.getX() + 1; i < rightUp.getX(); i++) {
            for (int j = leftDown.getY() + 1; j < rightUp.getY(); j++) {
                map[i][j] = Tileset.FLOOR;
            }
        }
    }

    private void drawVertically(TETile[][] map) {
        int min = leftDown.getY();
        int max = rightUp.getY();
        for (int i = min; i <= max; i++) {
            map[leftDown.getX()][i] = Tileset.WALL;
            map[rightUp.getX()][i] = Tileset.WALL;
        }
    }

    private void drawHorizontally(TETile[][] map) {
        int min = leftDown.getX();
        int max = rightUp.getX();
        for (int i = min; i <= max; i++) {
            map[i][leftDown.getY()] = Tileset.WALL;
            map[i][rightUp.getY()] = Tileset.WALL;
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
            return true;
        }
        // left neighbor
        if (r.rightUp.getX() < leftDown.getX()) {
            return false;
        }
        // right neighbor
        if (r.leftDown.getX() > rightUp.getX()) {
            return false;
        }
        // up neighbor
        if (r.leftDown.getY() > rightUp.getY()) {
            return false;
        }

        if (r.rightUp.getY() < leftDown.getY()) {
            return false;
        }

        return true;
    }
}
