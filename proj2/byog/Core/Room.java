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

    public void verticalHallways(List<Room> rooms, List<Hallway> hallways,
                                 Set<Room> connected, int height) {
        for (int i = getLeftDown().getX() + 1; i < getRightUp().getX(); i += 2) {
            Hallway hallway = new Hallway(new Position(i, 0), height - 2, true);
            boolean isConnected = false;
            for (Room room: rooms) {
                if (!connected.contains(room) && hallway.overlaps(room)) {
                    isConnected = true;
//                    if (hallway.getRightUp().getY() == height - 1 ||
//                    room.getRightUp().getY() > hallway.getRightUp().getY()) {
//                        hallway.getRightUp().setY(room.getRightUp().getY());
//                    }
//                    else if (hallway.getLeftDown().getY() == 0 ||
//                    room.getLeftDown().getY() < hallway.getLeftDown().getY()) {
//                        hallway.getLeftDown().setY(room.getLeftDown().getY());
//                    }
                    connected.add(room);
                }
            }
            if (isConnected) {
                hallways.add(hallway);
            }
        }
    }

    public void horizontalHallways(List<Room> rooms,
                                   List<Hallway> hallways, Set<Room> connected, int width) {
        for (int i = getLeftDown().getY() + 1; i < getRightUp().getY(); i += 2) {
            Hallway hallway = new Hallway(new Position(0, i), width - 2, false);
            boolean isConnected = false;
            for (Room room: rooms) {
                if (!connected.contains(room) && hallway.overlaps(room)) {
                    isConnected = true;
//                    if (hallway.getRightUp().getX() == width - 1 ||
//                    room.getRightUp().getX() > hallway.getRightUp().getX()) {
//                        hallway.getRightUp().setX(room.getRightUp().getX());
//                    }
//                    else if (hallway.getLeftDown().getX() == 0 ||
//                    room.getLeftDown().getX() < hallway.getLeftDown().getX()) {
//                        hallway.getLeftDown().setX(room.getLeftDown().getX());
//                    }
                    connected.add(room);
                }
            }
            if (isConnected) {
                hallways.add(hallway);
            }
        }
    }

}
