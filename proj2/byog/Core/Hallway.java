package byog.Core;

import byog.TileEngine.TETile;

public class Hallway extends Rectangle {
    private boolean isVertical;
    public Hallway(Position leftDown, int length, boolean isVertical) {
        this.leftDown = leftDown;
        this.isVertical = isVertical;
        if (isVertical) {
            this.rightUp = new Position(leftDown.getX() + 2,
                    leftDown.getY() + length + 1);
        } else {
            this.rightUp = new Position(leftDown.getX() + length + 1,
                    leftDown.getY() + 2);
        }
    }

    @Override
    public void drawEdge(TETile[][] map) {
        if (isVertical) {
            drawVertically(map);
        } else {
            drawHorizontally(map);
        }
    }
}
