package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MaxArrayDequeTest {
    
    @Test
    public void maxTest() {
        Comparator<Integer> cmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<>(cmp);
        maxArrayDeque.addFirst(14);
        maxArrayDeque.addFirst(17);
        maxArrayDeque.addFirst(20);
        maxArrayDeque.addFirst(11);
        
        assertEquals(20, (int)maxArrayDeque.max());
    }        
    
    
}
