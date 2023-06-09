import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractSkipList {
    final protected Node head;
    final protected Node tail;

    public AbstractSkipList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.dist = new ArrayList<>();
        tail.dist = new ArrayList<>();
        increaseHeight();
    }

    public void increaseHeight() {
        head.addLevel(tail, null);
        tail.addLevel(null, head);
        head.dist.add(0);
        tail.dist.add(Integer.MAX_VALUE);
    }

    abstract Node find(int key);

    abstract public int generateHeight();

    public Node search(int key) {
        Node curr = find(key);

        return curr.key() == key ? curr : null;
    }

    public Node insert(int key) {
        int nodeHeight = generateHeight();
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

        // Ranking maintenence(?) part
        ArrayList<Integer> lst = new ArrayList<>();
        prevNode = newNode.prev.get(0);
        int counter = 1;
        int level = 0;
        while (level <= nodeHeight) {
            if (prevNode == newNode.prev.get(level)) {
                lst.add(counter);
                newNode.getNext(level).dist.set(level, newNode.getNext(level).dist.get(level));
                level++;
            } else {
                counter += prevNode.dist.get(level - 1);
                prevNode = prevNode.prev.get(level - 1);
            }
        }
        level = 0;
        Node nextNode = newNode.getNext(level);
        while (level < Math.min(newNode.height(), newNode.getNext(level).height())) {
            nextNode.dist.set(level, 1);
            nextNode = newNode.getNext(level);
            level = level + 1;
        }
        level = newNode.height();
        nextNode = newNode.getNext(level);
        int t = 0;
        while (t == 0 && level <= head.height) {
            if (nextNode.height > level) {
                int i;
                for (i = level + 1; nextNode.height >= i; i++) {
                    nextNode.dist.set(i, nextNode.dist.get(i) + 1);
                }
                level = i - 1;
            }
            if (nextNode.height < head.height) {
                nextNode = nextNode.getNext(level);
            } else {
                t = 1;
            }
        }

        newNode.dist = lst;
        // End of Rank part

        return newNode;
    }

    public boolean delete(Node node) {
        for (int level = 0; level <= node.height(); ++level) {
            Node prev = node.getPrev(level);
            Node next = node.getNext(level);
            prev.setNext(level, next);
            next.setPrev(level, prev);
        }
        int level = node.height();
        Node next = node.getNext(level);
        int t = 0;
        while (t == 0 && level <= head.height) {
            if (next.height > level) {
                int i;
                for (i = level + 1; next.height >= i; i++) {
                	next.dist.set(i, next.dist.get(i) + (node.dist.get(Math.min(Math.max(node.height()-1,0), i))) -2);
                }
                level = i - 1;
            }
            if (next.height < head.height) {
            	next = next.getNext(level);
            } else {
                t = 1;
            }
        }
        return true;
    }
    /*
     * Node next = node.getNext(0);
        int i = 0;
        for ( ; i < node.height; i++)
        {
        	next = node.getNext(i);
        	next.dist.set(i, (node.dist.get(i)-1)+next.dist.get(i));
        }
        while (i < head.height)
        {
        	if (next.height > i) {
        		next.dist.set(i, (node.dist.get(Math.max(0,node.height-1))-1)+next.dist.get(i));
        		i++;
        	}
        	else
        	{
        		next = next.next.get(Math.max(0, i-1));
        	}
        }
     */

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
            curr = curr.getNext(level); // ADDED AND FIXED
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
        }

        public int height() {
            return height;
        }

        public int key() {
            return key;
        }
    }
}
