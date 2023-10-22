package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BSTMap<Key extends Comparable<Key>, Value>
        implements Map61B<Key, Value>{
    Node root;
    
    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        int size;
        
        public Node(Key key, Value value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }
    
    
    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(Key key) {
        return get(key) != null;
    }

    @Override
    public Value get(Key key) {
        return get(root, key);
    }
    
    private Value get(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = x.key.compareTo(key);
        if (cmp < 0) {
            return get(x.right, key);
        } else if (cmp > 0) {
            return get(x.left, key);
        } else {
            return x.value;
        }
    }

    @Override
    public int size() {
        return size(root);
    }
    
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    @Override
    public void put(Key key, Value value) {
        
        root = put(root, key, value);
    }
    
    private Node put(Node x, Key key, Value value) {
        if (value == null) {
            value = (Value) "null";
        }
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            x.left = put(x.left, key, value);
        } else if (cmp < 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    public Set<Key> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value remove(Key key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value remove(Key key, Value value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Key> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super Key> action) {
        Map61B.super.forEach(action);
    }

    @Override
    public Spliterator<Key> spliterator() {
        return Map61B.super.spliterator();
    }
    
    public void printInOrder() {
        printInOrder(root);
    }
    
    private void printInOrder(Node x) {
        if (x.left != null) {
            printInOrder(x.left);
        } 
        System.out.println(x.key + " " + x.value);
        if (x.right != null) {
            printInOrder(x.right);
        }
    }
}
