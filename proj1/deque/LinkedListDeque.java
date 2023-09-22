package deque;

import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<T> {
    private class StuffNode {
        StuffNode pre;
        StuffNode next;
        T item;
    }
    
    private StuffNode sentinel;
    private int size = 0;
    
    public LinkedListDeque() {
        sentinel = new StuffNode();
        sentinel.next =sentinel;
        sentinel.pre = sentinel;
    }
    
    public void addFirst(T item) {
        StuffNode addNode = new StuffNode();
        addNode.item = item;
        sentinel.next.pre = addNode;
        addNode.next = sentinel.next;
        sentinel.next = addNode;
        addNode.pre = sentinel;
        
        size = size + 1;
    }
    
    public void addLast(T item) {
        StuffNode addNode = new StuffNode();
        addNode.item = item;
        sentinel.pre.next = addNode;
        addNode.pre = sentinel.pre;
        sentinel.pre = addNode;
        addNode.next = sentinel;
        
        size = size + 1;
    }
    
    public boolean isEmpty() {
        if (size == 0) return true;
        else return false;
    }
    
    public int size() {
        return size;
    }
    
    public void printDeque() {
        StuffNode printNode = sentinel.next;
        while (printNode != sentinel) {
            System.out.println(printNode.item + " ");
            printNode = printNode.next;
        }
    }
    
    public T removeFirst() {
        if (size == 0) return null;
        T first = sentinel.next.item;
        sentinel.next.next.pre = sentinel;
        sentinel.next = sentinel.next.next;
        size = size - 1;
        return first;
    }
    
    public T removeLast() {
        if (size == 0) return null;
        T last = sentinel.pre.item;
        sentinel.pre.pre.next = sentinel;
        sentinel.pre = sentinel.pre.pre;
        size = size - 1;
        return last;
    }
    
    public T get(int index) {
        if (index >= size) return null;
        StuffNode getNode = sentinel.next;
        for (int i = 0; i < index; i ++ ) {
            getNode = getNode.next;
        }
        return getNode.item;
    }
    
    public T getRecursive(int index) {
        if (index >= size) return null;
        StuffNode getNode = sentinel.next;
        return getRecursiveHelper(index, getNode);
    }
    
    /** Make getNode variable inside the recursion, to down to next Node. */
    private T getRecursiveHelper(int index, StuffNode getNode) {
        if (index == 0) return getNode.item;
        getNode = getNode.next;
        return getRecursiveHelper(index - 1, getNode);
    }
    
    //public Iterator<T> iterator() {}
    
    public boolean equals(Object o) {
        if (!(o instanceof LinkedListDeque)) {
            return false;
        } else if (this.size != ((LinkedListDeque<?>) o).size) {
            return false;
        } else {
            for (int i = 0; i < this.size; i ++ ) {
                if (this.get(i) != ((LinkedListDeque<?>) o).get(i))
                    return false;
            }
            return true;
        }
    }
}
