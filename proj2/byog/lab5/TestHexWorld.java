package byog.lab5;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestHexWorld {
    @Test
    public void testHexagon2() {
        int x = 10;
        int y = 10;
        int s = 2;
//        int[][] expected = {{1,3},{0,4},{0,4},{1,3}};
        int [][] expected = {{x, x + 2}, {x - 1, x + 3}, {x - 1, x + 3}, {x, x + 2}};
        for (int i = 0; i < s + s; i++) {
            int begin = HexWorld.posOfBegin(i, s, x);
            int end = HexWorld.posOfEnd(i, s, x);
            assertEquals(expected[i][0], begin);
            assertEquals(expected[i][1], end);
        }
    }

    @Test
    public void testHexagon3() {
        int x = 10;
        int y = 10;
        int s = 3;
//        int xEnd = x + s + s;
//        int[][] expected = {{2,5},{1,6},{0,7},{0,7},{1,6},{2,5}};
        int[][] expected = {{x, x + 3}, {x - 1, x + 4}, {x - 2, x + 5},
                            {x - 2, x + 5}, {x - 1, x + 4}, {x, x + 3}};
        for (int i = 0; i < s + s; i++) {
            int begin = HexWorld.posOfBegin(i, s, x);
            int end = HexWorld.posOfEnd(i, s, x);
            assertEquals(expected[i][0], begin);
            assertEquals(expected[i][1], end);
        }
    }
}
