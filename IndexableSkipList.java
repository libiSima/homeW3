public class IndexableSkipList extends AbstractSkipList {
    final protected double probability;

    public IndexableSkipList(double probability) {
        super();
        this.probability = probability;
    }

    @Override
    public Node find(int val) {
        Node node = head;
        int level = head.height();
        while (level >= 0) {
            while (node.getNext(level) != tail && node.getNext(level).key() <= val)
                node = node.getNext(level);
            level = level - 1;
        }
        if (node.key() > val) {
            node = new Node(val);
        }
        return node;
    }

    @Override
    public int generateHeight() {
        int c = 0;
        double roll = (Math.random());
        while (roll < probability) {
            c++;
            roll = (Math.random());
        }
        return c;
    }

    public int rank(int val) {
        int rank = 0;
        Node node = this.head;
        int level = head.height();
        while(node.key() <= val){
            if(rank + node.getNext(level).dist.get(level)){
                level = level-1;
            }else{
                rank = rank + node.dist.get(level);
                node = node.getNext(level);
            }
            
        }
        return rank;
    }

    public int select(int index) {
        int i =0;
        Node node = this.head;
        int level = head.height();
        while (level >= 0) {
            while (node.getNext(level).dist.get(level) + i < index ){
                node = node.getNext(level);
                i = node.dist.get(level);
            }        
            level = level - 1;
        }
        return i;
    }
}
