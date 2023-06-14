import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;


    /*
     * You should add additional private members as needed.
     */
    private int elements;
    private ArrayList<Pair<K,V>> vals; //USE ARRAYLIST
    private int kcurr;

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        // Added
        this.elements = 0;
        vals = new ArrayList<Pair<K,V>>(capacity);
        for (int i = 0; i < capacity; i++)
        	vals.add(null);
        kcurr = k;
        //ASK ABOUT INITIALIZING IT
    }

    public V search(K key) {
    	int h = hashFunc.hash(key);
    	for (int i = 0 ; i < capacity; i++) {
    		if (vals.get(h)!=null && vals.get(h).first().equals(key))
    			return vals.get(h).second();
    		h = (int)HashingUtils.fastModularPower(((long)h+1), ((long)1), ((long)capacity));
    	}
    	return null;
    }

    public void insert(K key, V value) {
    	int h = hashFunc.hash(key);
    	for (int i = 0 ; i < capacity; i++) {
    		if (vals.get(h) == null) {
    			vals.set(h, new Pair<K,V>(key,value));
    			elements += 1;
    			if ((this.elements*1.0)/this.capacity > this.maxLoadFactor)
    				rehash();
    			return;
    		}
    		h = (int)HashingUtils.fastModularPower(((long)h+1), ((long)1), ((long)capacity));
    	}
    }

    public boolean delete(K key) {
    	int h = hashFunc.hash(key);
    	for (int i = 0 ; i < capacity; i++) {
    		if (vals.get(h)!=null && vals.get(h).first().equals(key)) {
    			vals.set(h, null);
    			elements -= 1;
    			return true;
    		}
    		h = (int)HashingUtils.fastModularPower(((long)h+1), ((long)1), ((long)capacity));
    	}
    	return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
    
    private void rehash()
    {
    	System.out.println("Rehashing with "+this.elements+" elements. (2^"+kcurr+" * maxMod = "+((1<<kcurr) * maxLoadFactor)+")");
    	kcurr++;
        this.capacity = 1<<kcurr;
        this.hashFunc = hashFactory.pickHash(kcurr);
        // Added
        ArrayList<Pair<K,V>> old = this.vals;
        this.vals = new ArrayList<Pair<K,V>>(capacity);
        for (int i = 0; i < capacity; i++)
        	vals.add(null);
        for (int i = 0; i < old.size(); i++)
        {
        	if (old.get(i) != null) 
        		this.insert(old.get(i).first(), old.get(i).second());
        }
    }
}
