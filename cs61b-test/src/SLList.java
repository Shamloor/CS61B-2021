/** An SLList is a list of integers, which hides the terrible truth
 *  of the nakedness within. */
public class SLList {
    private class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }
    
    private IntNode sentinel;
    private int size;
    
    public SLList(int x) {
        sentinel = new IntNode(63, null);
        sentinel.next = new IntNode(x, null);
        size += 1;
    }
    
    public SLList() {
        sentinel = new IntNode(63, null);
    }
    
    /** Adds x to the front of the list. */
    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size += 1;
    }
    
    /** Adds x to the last of the list. */
    public void addLast(int x) {
        
        IntNode p = sentinel;

        /** Move p until it reaches the end of the list. */
        while (p.next != null) {
            p = p.next;
        }
        p.next = new IntNode(x, null);
        size += 1;
    }
    
    
    /** Returns the first item of the list. */
    public int getFirst() {
        return sentinel.next.item;
    }
    
    /** Returns the last item of the list. */
    public int getLast() {
        IntNode p = sentinel.next;
        while (p.next != null) {
            p = p.next;
        }
        return p.item;
    }
    
    /** It is so slow, cannot handle with big data. 
     *  How to collect size quickly? Set a variable 'size', 
     *  plus one each time you add a new value.*/
    
//    public int size() {
//        return size(first);
//    }
    
//    private static int size(IntNode p) {
//        if (p.next == null)
//            return 1;
//        return 1 + size(p.next);
//    }
    public int size() {
        return size;
    }
    
    public static void main(String[] args) {
        /* Creates a list of one integer, namely 10 */
        SLList L = new SLList(10);
        SLList L1 = new SLList();
        L.addFirst(9);
        L1.addLast(8);
//        L.addLast1(20);
        System.out.println(L.getFirst());
        System.out.println(L1.getLast());
        System.out.println(L1.size());
    }

}
