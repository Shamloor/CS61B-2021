package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList{
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        correct.addLast(5);
        correct.addLast(10);
        correct.addLast(15);

        broken.addLast(5);
        broken.addLast(10);
        broken.addLast(15);

        assertEquals(correct.size(), broken.size());

        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
    }
    
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L1 = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L1.addLast(randVal);
                L2.addLast(randVal);
            } else if (operationNumber == 1 && L1.size() > 0) {
                int getVal1 = L1.getLast();
                int getVal2 = L2.getLast();
                assertEquals(getVal1, getVal2);
            } else if (operationNumber == 2 && L1.size() > 0) {
                int removeVal1 = L1.removeLast();
                int removeVal2 = L2.removeLast();
                assertEquals(removeVal1, removeVal2);
            } else if (operationNumber == 3) {
                // size
                int size1 = L1.size();
                int size2 = L2.size();
                assertEquals(size1, size2);
            } else if (operationNumber == 4) {
                int randidx = StdRandom.uniform(0, 250);
                if (randidx < L1.size() && randidx < L2.size()) {
                    int getVal1 = L1.get(randidx);
                    int getVal2 = L2.get(randidx);
                    assertEquals(getVal1, getVal2);
                }
            }
        }
    }
}
