package creatures;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(1.97, p.energy(), 0.001);
        p.move();
        assertEquals(1.94, p.energy(), 0.001);
        p.stay();
        assertEquals(1.93, p.energy(), 0.001);
        p.stay();
        assertEquals(1.92, p.energy(), 0.001);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        Clorus r = p.replicate();
        assertNotSame("replicate should not be the same", p, r);
        assertEquals(1.0d, p.energy(), 0.01);
        assertEquals(1.0d, r.energy(), 0.01);
    }

    @Test
    public void testChoose() {
        Clorus p;

        p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded;
        surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());
        assertEquals(new Action(Action.ActionType.STAY), p.chooseAction(surrounded));

        surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Empty());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());
        assertEquals(new Action(Action.ActionType.ATTACK, Direction.TOP),
                p.chooseAction(surrounded));

        surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Empty());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());
        assertEquals(new Action(Action.ActionType.REPLICATE, Direction.TOP),
                p.chooseAction(surrounded));
    }
}
