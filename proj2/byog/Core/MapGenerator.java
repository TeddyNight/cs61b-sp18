package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class MapGenerator implements java.io.Serializable {
    private int width;
    private int height;
    private TETile[][] map;
    private Random r;
    private Player player = null;

    public MapGenerator(long seed, int width, int height) {
        this.width = width;
        this.height = height;
        r = new Random(seed);
        map = new TETile[width][height];
        init();
        List<Room> rooms = generateRooms();
        List<Hallway> hallways = addHallways(rooms);
        for (Room room: rooms) {
            room.drawEdge(map);
        }
        for (Hallway hallway: hallways) {
            hallway.drawEdge(map);
        }
        for (Hallway hallway: hallways) {
            hallway.drawFloor(map);
        }
        for (Room room: rooms) {
            room.drawFloor(map);
        }
    }

    public List<Hallway> addHallways(List<Room> rooms) {
        /**
         * TODO Hallways that connects two adjacent rooms
         * 1. Select one direction (up/down/right/left)
         * 2. List all possible hallways on that direction
         * 3. Connect most adjacent rooms
         * 4. Hallway ends at another side of room
         */
        List<Hallway> hallways = new ArrayList<>();
        Set<Room> connected = new HashSet<>();
        for (Room room: rooms) {
            if (connected.contains(room)) {
                continue;
            }
            room.verticalHallways(rooms, hallways, connected, this.height);
            connected.remove(room);
            room.horizontalHallways(rooms, hallways, connected, this.width);
        }
        return hallways;
    }

    private void init() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }

    }

    public TETile[][] getMap() {
        return map;
    }

    private List<Room> generateRooms() {
        int num = 25;
        List<Room> rooms = new ArrayList<Room>();
        while (rooms.size() != num) {
            Room room = randomRoom(rooms);
            if (!room.overlaps(rooms)) {
                rooms.add(room);
            }
        }
        return rooms;
    }


    private Position randomPoint() {
        return new Position(r.nextInt(this.width - 3) + 3, r.nextInt(this.height - 3) + 3);
    }

    private Position randomPoint(Position p) {
        return new Position(r.nextInt(p.getX()), r.nextInt(p.getY()));
    }

    private Room randomRoom(List<Room> rooms) {
        Room room = null;
        Position rightUp = randomPoint();
        Position leftDown = randomPoint(rightUp);
        while (leftDown.distanceX(rightUp) < 3
                || leftDown.distanceY(rightUp) < 3
                || leftDown.distanceX(rightUp) > this.width / 6
                || leftDown.distanceY(rightUp) > this.width / 6) {
            leftDown = randomPoint(rightUp);
        }
        room = new Room(leftDown, rightUp);
        return room;
    }

    public Player getPlayer() {
        if (player == null) {
            Position pos = randomPoint();
            while (map[pos.getX()][pos.getY()] != Tileset.FLOOR) {
                pos = randomPoint();
            }
            player = new Player(pos, width, height, map);
        }
        return player;
    }
}
