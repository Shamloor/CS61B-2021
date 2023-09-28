package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {
    @Test
    public void addIsEmptySizeTestWithoutResize() {
        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        
        lld1.addFirst("front");
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());
        
        lld1.addLast("middle");
        assertEquals(2, lld1.size());
        lld1.addLast("back");
        
        assertEquals(3, lld1.size());
    }

    @Test
    public void addRemoveTestWithoutResize() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        
        lld1.addFirst(10);
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());
        
        int rmvVal = lld1.removeFirst();
        assertEquals(10, rmvVal);
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    public void removeEmptyTestWithoutResize() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTestWithoutResize() {
        ArrayDeque<String>  lld1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld2.addLast(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    public void addLastTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 50000; i ++ ) {
            lld1.addLast(i);
            lld1.addFirst(i);
        }
    }
    
    @Test
    public void edgeSituationTest() {
        Deque<Integer> arrayDeque = new ArrayDeque<>();
        
        for (int i = 0; i < 9; i ++ )
            arrayDeque.addFirst(i + 1);
        
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        
        arrayDeque.addLast(0);
        arrayDeque.addLast(18);
        int getVal = arrayDeque.get(0);
        int getVal2 = arrayDeque.get(3);
        assertEquals(6, getVal);
        assertEquals(3, getVal2);
    }
    
    @Test
    public void equalsTest() {
        Deque<Integer> arrayDeque = new ArrayDeque<>();
        Deque<Integer> cmpDeque = new LinkedListDeque<>();
        arrayDeque.addFirst(17);
        arrayDeque.addFirst(18);
        arrayDeque.addFirst(19);
        
        cmpDeque.addFirst(17);
        cmpDeque.addFirst(18);
        cmpDeque.addFirst(19);
        
        
        assertTrue("Two deques are the same.", arrayDeque.equals(cmpDeque));
        
    }
}
