package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private int initialSize;
    private double loadFactor;
    private Collection<Node>[] buckets;
    private int numOfBuckets;
    private Set<K> keySet = new HashSet<>();
    private Set<Node> nodeSet = new HashSet<>();
    

    /** Constructors */
    public MyHashMap() {
        this.initialSize = 16;
        this.loadFactor = 0.75;
        this.buckets = createTable(this.initialSize);
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(this.initialSize);
    }
    
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
    }
    
    
    
    
    @Override
    public void clear() {
        this.buckets = createTable(initialSize);
        this.numOfBuckets = 0;
        this.keySet = createKeySet();
        this.nodeSet = createNodeSet();
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        Collection<Node> collection = this.buckets[getHashCode(key)];
        if (collection == null) {
            return null;
        }
        Iterator<Node> it = collection.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.keySet.size();
    }

    @Override
    public void put(K key, V value) {
        if (overSize()) {
            resize();
        }
        
        int hashCode = getHashCode(key);
        if (this.buckets[hashCode] == null) {
            this.buckets[hashCode] = new LinkedList();
            numOfBuckets += 1;
        }
        Node newNode = createNode(key, value);
        if (containsKey(key)) {
            this.buckets[hashCode].remove(getSpecifiedNode(key));
            this.nodeSet.remove(getSpecifiedNode(key));
        }
        this.buckets[hashCode].add(newNode);
        this.nodeSet.add(newNode);
        this.keySet.add(newNode.key);
    }

    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private int getHashCode(K key) {
        return Math.floorMod(key.hashCode(), initialSize);
    }

    private Node getSpecifiedNode(K key) {
        Iterator<Node> iter = nodeSet.iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private Collection<Node>[] createTable(int tableSize) {
        return new LinkedList[tableSize];
    }

    private Set<K> createKeySet() {
        return new HashSet<>();
    }

    private Set<Node> createNodeSet() {
        return new HashSet<>();
    }
    
    private void resize() {
        initialSize *= 2;
        Set<K> tmpKeySet = keySet;
        Set<Node> tmpNodeSet = nodeSet;
        clear();
        Iterator<Node> iter = tmpNodeSet.iterator();;
        while (iter.hasNext()) {
            Node node = iter.next();
            put(node.key, node.value);
        }
    }
    
    private boolean overSize() {
        return numOfBuckets != 0 && this.keySet.size()
                / (double)initialSize >= loadFactor;
    }
}
