import java.util.Random;

public class IndexableSkipList extends AbstractSkipList {
    final protected double probability;

    public IndexableSkipList(double probability) {
        super();
        this.probability = probability;
    }

    @Override
    public Node find(int val) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    @Override
    public int generateHeight() {
        Random random = new Random();
        int height = 1;
        while (random.nextDouble() < probability) {
            height = height + 1;
        }
        return height;
    }

    public int rank(int val) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }
}
