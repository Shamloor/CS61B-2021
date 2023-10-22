package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BSTMap<K extends Comparable<K>, V>
        implements Map61B<K, V>{
    Node root;
    
    private class Node {
        K k;
        V v;
        Node left;
        Node right;
        int size;
        
        public Node(K k, V v, int size) {
            this.k = k;
            this.v = v;
            this.size = size;
        }
    }
    
    
    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K k) {
        return get(k) != null;
    }

    @Override
    public V get(K k) {
        return get(root, k);
    }
    
    private V get(Node x, K k) {
        if (x == null) {
            return null;
        }
        int cmp = x.k.compareTo(k);
        if (cmp < 0) {
            return get(x.right, k);
        } else if (cmp > 0) {
            return get(x.left, k);
        } else {
            return x.v;
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
    public void put(K k, V v) {
        
        root = put(root, k, v);
    }
    
    private Node put(Node x, K k, V v) {
        if (v == null) {
            v = (V) "null";
        }
        if (x == null) {
            return new Node(k, v, 1);
        }
        int cmp = x.k.compareTo(k);
        if (cmp > 0) {
            x.left = put(x.left, k, v);
        } else if (cmp < 0) {
            x.right = put(x.right, k, v);
        } else {
            x.v = v;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        Map61B.super.forEach(action);
    }

    @Override
    public Spliterator<K> spliterator() {
        return Map61B.super.spliterator();
    }
    
    public void printInOrder() {
        printInOrder(root);
    }
    
    private void printInOrder(Node x) {
        if (x.left != null) {
            printInOrder(x.left);
        } 
        System.out.println(x.k + " " + x.v);
        if (x.right != null) {
            printInOrder(x.right);
        }
    }
}
