package deque;

import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<T> implements Deque<T> {
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

    @Override
    public void addFirst(T item) {
        StuffNode addNode = new StuffNode();
        addNode.item = item;
        sentinel.next.pre = addNode;
        addNode.next = sentinel.next;
        sentinel.next = addNode;
        addNode.pre = sentinel;
        
        size = size + 1;
    }

    @Override
    public void addLast(T item) {
        StuffNode addNode = new StuffNode();
        addNode.item = item;
        sentinel.pre.next = addNode;
        addNode.pre = sentinel.pre;
        sentinel.pre = addNode;
        addNode.next = sentinel;
        
        size = size + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        StuffNode printNode = sentinel.next;
        while (printNode != sentinel) {
            System.out.println(printNode.item + " ");
            printNode = printNode.next;
        }
    }

    @Override
    public T removeFirst() {
        if (size == 0) return null;
        T first = sentinel.next.item;
        sentinel.next.next.pre = sentinel;
        sentinel.next = sentinel.next.next;
        size = size - 1;
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;
        T last = sentinel.pre.item;
        sentinel.pre.pre.next = sentinel;
        sentinel.pre = sentinel.pre.pre;
        size = size - 1;
        return last;
    }

    @Override
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
    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    
    private class LinkedListDequeIterator implements Iterator<T> {
        private int wizPos;
        public LinkedListDequeIterator() {wizPos = 0;}
        @Override
        public boolean hasNext() {return wizPos < size;}
        @Override
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != this.getClass()) return false;
        LinkedListDeque<T> other = (LinkedListDeque<T>) o;
        if (other.size() != this.size()) return false;
        for (int i = 0; i < this.size(); i ++ ) {
            if (this.get(i) != other.get(i)) return false;
        }
        return true;
    }
}
