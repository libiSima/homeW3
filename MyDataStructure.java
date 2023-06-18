import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class MyDataStructure {
    /*
     * You may add any members that you wish to add.
     * Remember that all the data-structures you use must be YOUR implementations,
     * except for the List and its implementation for the operation Range(low, high).
     */
	HashTable<Integer, AbstractSkipList.Node> hash;
    IndexableSkipList skipList;

    /***
     * This function is the Init function described in Part 4.
     *
     * @param N The maximal number of items expected in the DS.
     */
    public MyDataStructure(int N) {
    	hash = new ChainedHashTable(new ModularHash());
        skipList = new IndexableSkipList(0.5);
    }

    /*
     * In the following functions,
     * you should REMOVE the place-holder return statements.
     */
    public boolean insert(int value) {
        if (this.contains(value)) return false;
        this.skipList.insert(value);
        this.hash.insert(value, skipList.search(value));
        return true;
    }

    public boolean delete(int value) {
    	if(this.contains(value)){
            AbstractSkipList.Node tod = hash.search(value);
            skipList.delete(tod);
            return true;
        }
        return false;
    }

    public boolean contains(int value) {
        return (hash.search(value) != null);
    }

    public int rank(int value) {
    	return skipList.rank(value);
    }

    public int select(int index) {
        return skipList.select(index);
    }

    public List<Integer> range(int low, int high) {
    	if(! this.contains(low)){
            return null;
        }
        AbstractSkipList.Node tod = hash.search(low);
        LinkedList<Integer> ans = new LinkedList();
        while(tod.key() <= high){
            ans.addLast(tod.key());
            tod = tod.getNext(0);

        }
        return ans;
    }
}
