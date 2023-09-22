package deque;


public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int first = 5;
    private int last = 6;
    
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
    }
    
    public void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = (first + 1) % items.length, j = 1; i <= first + size; i ++ , j ++ ) {
            a[j] = items[i % items.length];
        }
        items = a;
        first = 0;
        last = size + 1;
    }
    
    public void addFirst(T item) {
        items[first] = item;
        size = size + 1;

        first = (first - 1 + items.length) % items.length;
        
        if (size == items.length) {
            resize(size * 2);
        }
    }
    
    public void addLast(T item) {
        items[last] = item;
        size = size + 1;

        last = (last + 1) % items.length;
        
        if (size == items.length) {
            resize(size * 2);
        }
    }
    
    public boolean isEmpty() {
        if (size == 0) return true;
        else return false;
    }
    
    public int size() {
        return size;
    }
    
    public void printDeque() {
        resize(size + 1);
        for (int i = 1; i <= size; i ++ ) {
            System.out.println(items[i] + " ");
        }
    }
    
    public T removeFirst() {
        if (size == 0) return null;
        
        T removeVal = items[(first + 1) % items.length];
        items[(first + 1) % items.length] = null;
        size = size - 1;
        
        first = (first + 1) % items.length;
        
        if(size < items.length / 4 && size > 4) {
            resize(items.length / 4);
        }
        
        return removeVal;
    }

    public T removeLast() {
        if (size == 0) return null;
        
        T removeVal = items[(last - 1 + items.length) % items.length];
        items[(last - 1 + items.length) % items.length] = null;
        size = size - 1;
        
        last = (last - 1 + items.length) % items.length;
        
        if(size < items.length / 4 && size > 4) {
            resize(items.length / 4);
        }
        
        return removeVal;
    }
    
    public T get(int index) {
        resize(size + 1);
        return items[index + 1];
    }

    public boolean equals(Object o) {
        if (!(o instanceof ArrayDeque)) {
            return false;
        } else if (this.size != ((ArrayDeque<?>) o).size) {
            return false;
        } else {
            for (int i = 0; i < this.size; i ++ ) {
                if (this.get(i) != ((ArrayDeque<?>) o).get(i))
                    return false;
            }
            return true;
        }
    }
}
