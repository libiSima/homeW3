import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractSkipList {
    final protected Node head;
    final protected Node tail;

    public AbstractSkipList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        increaseHeight();
    }

    public void increaseHeight() {
        head.addLevel(tail, null);
        tail.addLevel(null, head);
    }

    abstract Node find(int key);

    abstract public int generateHeight();

    public Node search(int key) {
        Node curr = find(key);

        return curr.key() == key ? curr : null;
    }

    public Node insert(int key) {
        int nodeHeight = generateHeight();

        System.out.println("Height "+nodeHeight+" Generated.");
        while (nodeHeight > head.height()) {
            increaseHeight();
        }

        Node prevNode = find(key);
        if (prevNode.key() == key) {
            return null;
        }

        Node newNode = new Node(key);

        for (int level = 0; level <= nodeHeight && prevNode != null; ++level) {
            Node nextNode = prevNode.getNext(level);

            newNode.addLevel(nextNode, prevNode);
            prevNode.setNext(level, newNode);
            nextNode.setPrev(level, newNode);

            while (prevNode != null && prevNode.height() == level) {
                prevNode = prevNode.getPrev(level);
            }
        }

        return newNode;
    }

    public boolean delete(Node node) {
    	for (int level = 0; level <= node.height(); ++level) {
            Node prev = node.getPrev(level);
            Node next = node.getNext(level);
            prev.setNext(level, next);
            next.setPrev(level, prev);
        }

        return true;
    }

    public int predecessor(Node node) {
        return node.getPrev(0).key();
    }

    public int successor(Node node) {
        return node.getNext(0).key();
    }

    public int minimum() {
        if (head.getNext(0) == tail) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return head.getNext(0).key();
    }

    public int maximum() {
        if (tail.getPrev(0) == head) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return tail.getPrev(0).key();
    }

    private void levelToString(StringBuilder s, int level) {
        s.append("H    ");
        Node curr = head.getNext(level);

        while (curr != tail) {
            s.append(curr.key);
            s.append("    ");
            curr = curr.getNext(level); //ADDED AND FIXED
        }

        s.append("T\n");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int level = head.height(); level >= 0; --level) {
            levelToString(str, level);
        }

        return str.toString();
    }
    
    /////REMOVE THISSSSS
    public void printAllDistances()
    {
    	String s = "";
    	Node n = head;
    	while (n != tail) {
    		s += "("+n.key+","+n.printAllDist()+")"+", ";
    		n=n.getNext(0);
    	}
    	System.out.println(s);
    }

    public static class Node {
        final private List<Node> next;
        final private List<Node> prev;
        private int height;
        final private int key;
        protected ArrayList<Integer> dist;

        public Node(int key) {
            next = new ArrayList<>();
            prev = new ArrayList<>();
            this.height = -1;
            this.key = key;
            dist = new ArrayList<>();
        }

        public Node getPrev(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return prev.get(level);
        }

        public Node getNext(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return next.get(level);
        }

        public void setNext(int level, Node next) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.next.set(level, next);
        }

        public void setPrev(int level, Node prev) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.prev.set(level, prev);
        }

        public void addLevel(Node next, Node prev) {
            ++height;
            this.next.add(next);
            this.prev.add(prev);
            this.updateAndCalcDistsInsert();
        }
        
        private void updateAndCalcDistsInsert()
        {
        	this.dist = new ArrayList<>(height);
        	System.out.println("Node "+this.key+" Updating");
        	if (this.key == Integer.MIN_VALUE || this.key == Integer.MAX_VALUE) {
        		for (int i = 0; i < height; i++)
        			this.dist.add(this.key == Integer.MAX_VALUE ? Integer.MAX_VALUE : 0);
        		return;
        	}
        	if (height == 0) {
        		this.dist.add(1);
        		return;
        	}
        	System.out.println("height = "+height+" \nThis.next = "+printLst(this.next)+"\nthis.prev = "+printLst(this.prev));
        	Node previousNode = this.getPrev(0);
        	int counter = 1;
        	int level = 0;
        	while (level < height)
        		if (this.prev.get(level) == previousNode)
        		{
        			this.dist.add(counter);
        			level++;
        		}
        		else
        		{
        			counter += previousNode.dist.get(level);
        			previousNode=previousNode.getPrev(level);
        		}
        	this.next.get(height-1).dist.set(height-1, this.next.get(height-1).dist.get(height-1)-counter);
        }
        /*
        private void updateAndCalcDistsInsert()
        {
        	System.out.println("Node "+this.key+" Updating");
        	if (this.key == Integer.MIN_VALUE || this.key == Integer.MAX_VALUE) {
        		this.dist.add(0);
        		return;
        	}
        	if (height == 0) {
        		this.dist.add(1);
        		return;
        	}
        	System.out.println("height = "+height+" \nThis.next = "+printLst(this.next)+"\nthis.prev = "+printLst(this.prev));
        	Node previousNode = this.getPrev(height-1); //the one before the layer we added
        	int counter = this.dist.get(height-1);
        	while (previousNode != prev.get(height))
        	{
        		counter += previousNode.dist.get(height-1);
        		previousNode = previousNode.getPrev(height-1);
        	}
        	this.dist.add(counter);
        	this.next.get(height-1).dist.set(height-1, this.next.get(height-1).dist.get(height-1)-counter);
        }
        */

        public int height() { return height; }
        public int key() { return key; }
        
        private static <T> String printLst(List<T> lst)
        {
        	String str = "[";
        	for (T t:lst)
        		str += t.toString()+",";
        	return str+"]";
        }
        public String printAllDist()
        {
        	String str = "[";
        	for (int t:dist)
        		str += t+",";
        	return str+"]";
        }
    }
}
