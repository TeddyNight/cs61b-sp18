package byog.Core;

import java.util.List;
import java.util.Set;

public class Room extends Rectangle {

    public Room(Position leftDown, int width, int height) {
        this.leftDown = leftDown;
        this.rightUp = new Position(leftDown.getX() + width + 1, leftDown.getY() + height + 1);
    }

    public Room(Position leftDown, Position rightUp) {
        this.leftDown = leftDown;
        this.rightUp = rightUp;
    }

    private boolean isLeftNeighbor(Room room) {
        return this.leftDown.getX() >= room.rightUp.getX();
    }

    private boolean isRightNeighbor(Room room) {
        return this.rightUp.getX() <= room.rightUp.getX();
    }

    private boolean isUpNeighbor(Room room) {
        return this.rightUp.getY() <= room.leftDown.getY();
    }

    private boolean isDownNeighbor(Room room) {
        return this.leftDown.getY() >= room.rightUp.getY();
    }

    public void verticalHallway(List<Room> rooms, List<Hallway> hallways,
                                 Set<Room> connected, int height) {
        for (int i = getLeftDown().getX() + 1; i < getRightUp().getX(); i += 2) {
            Hallway hallway = new Hallway(new Position(i, 0), height - 2, true);
            for (Room room: rooms) {
                if (room == this) {
                    continue;
                }
                if (!connected.contains(room) && hallway.overlaps(room)) {
                    connected.add(room);
                    hallways.add(hallway);
                    if (isUpNeighbor(room)) {
                        hallway.leftDown.setY(this.rightUp.getY() - 2);
                        hallway.rightUp.setY(room.leftDown.getY() + 2);
                    } else if (isDownNeighbor(room)) {
                        hallway.leftDown.setY(room.rightUp.getY() - 2);
                        hallway.rightUp.setY(this.leftDown.getY() + 2);
                    }
                    return;
                }
            }
        }
    }

    public void horizontalHallway(List<Room> rooms,
                                   List<Hallway> hallways, Set<Room> connected, int width) {
        for (int i = getLeftDown().getY() + 1; i < getRightUp().getY(); i += 2) {
            Hallway hallway = new Hallway(new Position(0, i), width - 2, false);
            for (Room room: rooms) {
                if (room == this) {
                    continue;
                }
                if (!connected.contains(room) && hallway.overlaps(room)) {
                    connected.add(room);
                    hallways.add(hallway);
                    if (isRightNeighbor(room)) {
                        hallway.leftDown.setX(this.rightUp.getX() - 2);
                        hallway.rightUp.setX(room.leftDown.getX() + 2);
                    } else if (isLeftNeighbor(room)) {
                        hallway.leftDown.setX(room.rightUp.getX() - 2);
                        hallway.rightUp.setX(this.leftDown.getX() + 2);
                    }
                    return;
                }
            }
        }
    }

}
