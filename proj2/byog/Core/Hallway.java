package byog.Core;

public class Hallway extends Rectangle {
    public Hallway(Position leftDown, int length, boolean isVertical) {
        this.leftDown = leftDown;
        if (isVertical) {
            this.rightUp = new Position(leftDown.getX() + 2,
                    leftDown.getY() + length + 1);
        } else {
            this.rightUp = new Position(leftDown.getX() + length + 1,
                    leftDown.getY() + 2);
        }
    }

}
