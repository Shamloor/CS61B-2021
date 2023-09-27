package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    /** Two pointers : first and last.
     *  Left to Right till length - 1 , then turn back to front. 
     *  */
    private int first = 5;
    private int last = 6;
    
    
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
    }
    
    /** How to resize the ArrayDeque ? 
     *  1. Receive a parameter which is wanted items.lenth.
     *  2. Create a new T list. 
     *  3. Put the original T list elements to new list. 
     *  4. Replace the orininal list with new list. 
     *  
     *  Variable first 
     *  */
    public void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = first + 1, j = 1; i <= first + size; i++ , j++) {
            a[j] = items[i % items.length];
        }
        items = a;
        first = 0;
        last = size + 1;
    }
    
    
    /** When resize :
     *  1. Check whether size equals to items.length. 
     *     If ture, resize the arraylist. 
     *  2. Get the variable first from resize(), which equals to zero. 
     *     items[0] = item.
     *  3. size += 1.
     *  4. first equals to items.length - 1. */
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        
        items[first] = item;
        size = size + 1;

        first = (first - 1 + items.length) % items.length;
    }

    /** When resize :
     *  1. Check whether size equals to items.length. 
     *     If ture, resize the arraylist. 
     *  2. Get the variable last from resize(), which equals to size + 1. 
     *     items[size + 1] = item.
     *  3. size += 1.
     *  4. last equals to (last + 1) % items.length. */
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        
        items[last] = item;
        size = size + 1;

        last = (last + 1) % items.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        resize(items.length);
        for (int i = 1; i <= size; i++) {
            System.out.println(items[i] + " ");
        }
    }
    
    /** When removeFirst :
     *  1. Consider the empty list.
     *  2. Get the first value.
     *  3. 
     * */
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        
        T removeVal = items[(first + 1) % items.length];
        items[(first + 1) % items.length] = null;
        size = size - 1;
        
        first = (first + 1) % items.length;
        
        if(size < items.length / 4 && size > 4) {
            resize(items.length / 4);
        }
        
        return removeVal;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        
        T removeVal = items[(last - 1 + items.length) % items.length];
        items[(last - 1 + items.length) % items.length] = null;
        size = size - 1;
        
        last = (last - 1 + items.length) % items.length;
        
        if(size < items.length / 4 && size > 4) {
            resize(items.length / 4);
        }
        
        return removeVal;
    }

    @Override
    public T get(int index) {
        return items[(index + first + 1) % items.length];
    }
    
    
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int wizPos;
        public ArrayDequeIterator() {wizPos = 0;}
        @Override
        public boolean hasNext() {return wizPos < size;}
        @Override
        public T next() {
            T returnItem = items[wizPos];
            wizPos += 1;
            return returnItem;
        }
    }
    
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != this.getClass()) return false;
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if (other.size() != this.size()) return false;
        for (int i = 0; i < this.size(); i++) {
            if (other.get(i) != this.get(i)) return false;
        }
        return true;
    }
}
