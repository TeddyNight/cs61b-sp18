package synthesizer;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void basicTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer<Integer>(10);
        int[] test = {1,2,3,4,5,6,7,8,9,10};
        for (int i = 0; i < 10; i++) {
            arb.enqueue(test[i]);
        }
        assertEquals(10,arb.fillCount());
        assertEquals(true,arb.isFull());
        assertEquals(false,arb.isEmpty());
        for (int i = 0; i < 10; i++) {
            assertEquals(test[i],arb.dequeue());
        }
        assertEquals(0,arb.fillCount());
        assertEquals(true,arb.isEmpty());
        assertEquals(false,arb.isFull());
    }

    @Test
    public void peekTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer<Integer>(10);
        arb.enqueue(1);
        assertEquals(1,arb.peek());
    }

    @Test(expected = RuntimeException.class)
    public void emptyDequeTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer<Integer>(10);
        arb.dequeue();
    }

    @Test(expected = RuntimeException.class)
    public void emptyPeekTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer<Integer>(10);
        arb.peek();
    }

    @Test(expected = RuntimeException.class)
    public void fullTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer<Integer>(10);
        int[] test = {1,2,3,4,5,6,7,8,9,10};
        for (int i = 0; i < 10; i++) {
            arb.enqueue(test[i]);
        }
        arb.enqueue(11);
    }

    @Test
    public void ringTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer<Integer>(3);
        arb.enqueue(1);
        arb.dequeue();
        arb.enqueue(1);
        arb.dequeue();
        arb.enqueue(1);
        arb.dequeue();
        arb.enqueue(1);
        assertEquals(1,arb.dequeue());
    }

    @Test
    public void iteratorTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(10);
        int[] test = {1,2,3,4,5,6,7,8,9,10};
        for (int i = 0; i < 10; i++) {
            arb.enqueue(test[i]);
        }
        int j = 0;
        for (Integer x: arb) {
            assertEquals((Integer) test[j], x);
            j = j + 1;
        }
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
