import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private List<LinkedList<Pair<K, V>>> hashTable;
    private int n = 0;

    /*
     * You should add additional private members as needed.
     */

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        hashTable = new ArrayList<LinkedList<Pair<K, V>>>(k);
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
    }

    public V search(K key) {
        int index = hashFunc.hash(key);
        List<Pair<K, V>> listf = hashTable.get(index);
        for (Pair<K, V> pair : listf) {
            if (pair.first() == key) {
                return pair.second();
            }
        }
        return null;
    }

    public void insert(K key, V value) {
        Pair<V, K> newp = new Pair(key, value);
        int index = hashFunc.hash(key);
        LinkedList listf = hashTable.get(index);
        listf.add(newp);
        n = n + 1;
        if (n * maxLoadFactor > capacity) {
            this.rehashing();
        }

    }

    private void rehashing() {
        this.hashFunc = hashFactory.pickHash(hashTable.size() * 2);
        List<LinkedList<Pair<K, V>>> oldList = this.hashTable;
        this.hashTable = new ArrayList<LinkedList<Pair<K, V>>>(hashTable.size() * 2);
        this.capacity = this.capacity * 2;
        for (List<Pair<K, V>> list : oldList) {
            for (Pair<K, V> pair : list) {
                this.insert(pair.first(), pair.second());
            }
        }
    }

    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        List<Pair<K, V>> listf = hashTable.get(index);
        for (Pair<K, V> pair : listf) {
            if (pair.first() == key) {
                listf.remove(pair);
                n = n - 1;
                return true;
            }
        }
        return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() {
        return capacity;
    }
}
