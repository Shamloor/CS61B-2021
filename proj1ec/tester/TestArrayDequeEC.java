package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.Deque;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {
    @Test
    public void testAddRemove() {
        StudentArrayDeque<Integer> stdArray = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> soluArray = new ArrayDequeSolution<>();
        ArrayDequeSolution<String> failure = new ArrayDequeSolution<>();
        
        int looptime = 500;
        String message;
        
        for (int i = 0; i < looptime; i ++ ) {
            int choice = StdRandom.uniform(0, 4);
            
            switch (choice) {
                case 0:
                    int seed1 = StdRandom.uniform(0, 100);
                    stdArray.addFirst(seed1);
                    soluArray.addFirst(seed1);
                    failure.addLast("addFirst(" + seed1 + ")");
                    break;
                case 1:
                    int seed2 = StdRandom.uniform(0, 100);
                    stdArray.addLast(seed2);
                    soluArray.addLast(seed2);
                    failure.addLast("addLast(" + seed2 + ")");
                    break;
                case 2:
                    if (!stdArray.isEmpty() && !soluArray.isEmpty()) {
                        Integer stdVal = stdArray.removeFirst();
                        Integer soluVal = soluArray.removeFirst();
                        
                        failure.addLast("removeFirst()");
                        assertEquals(getFailureMessage(failure), soluVal, stdVal);
                    }
                    break;
                case 3:
                    if (!stdArray.isEmpty() && !soluArray.isEmpty()) {
                        Integer stdVal = stdArray.removeLast();
                        Integer soluVal = soluArray.removeLast();
                        failure.addLast("removeLast()");
                        assertEquals(getFailureMessage(failure), soluVal, stdVal);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private String getFailureMessage(ArrayDequeSolution<String> failure) {
        String failMessage = new String();
        int looptime = failure.size();
        for (int i = 0; i < looptime; i ++ ) {
            String singleMessage = failure.get(i);
            failMessage = failMessage.concat(singleMessage + "\n");
        }
        return failMessage;
    }

    /** @source AssertEqualsStringDemo.java */
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeEC.class);
    }
}
