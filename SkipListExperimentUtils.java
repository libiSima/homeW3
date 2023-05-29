public class SkipListExperimentUtils {
     public static double measureLevels(double p, int x) {
        double avg = 1;
        for (int i = 0; i < x; i = i + 1) {
            int height = 0;
            while (Math.random() < p) {
                height = height + 1;
            }
            avg = avg + height;
        }
        return avg / x;
    }

    /*
     * The experiment should be performed according to these steps:
     * 1. Create the empty Data-Structure.
     * 2. Generate a randomly ordered list (or array) of items to insert.
     *
     * 3. Save the start time of the experiment (notice that you should not
     *    include the previous steps in the time measurement of this experiment).
     * 4. Perform the insertions according to the list/array from item 2.
     * 5. Save the end time of the experiment.
     *
     * 6. Return the DS and the difference between the times from 3 and 5.
     */
    public static Pair<AbstractSkipList, Double> measureInsertions(double p, int size) {
        AbstractSkipList skipList = new IndexableSkipList(p);
        double alltime = 0;
        int startine, endtime;
        for (int i = 0; i <= size; i = i + 1) {
            double num = Math.random();
            int n = (int) (num * size * 2);
            startine = (int) System.nanoTime();
            skipList.insert(n);
            endtime = (int) System.nanoTime();
            alltime = alltime + (startine - endtime);
        }
        Pair<AbstractSkipList, Double> pair = new Pair<>(skipList, alltime / (size + 1));
        return pair;
    }

    public static double measureSearch(AbstractSkipList skipList, int size) {
        Random rand = new Random();
        double alltime = 0;
        int startine, endtime;
        for (int i = 0; i <= size; i = i + 1) {
            int num = rand.nextInt(size * 2);
            startine = (int) System.nanoTime();
            skipList.search(num);
            endtime = (int) System.nanoTime();
            alltime = alltime + (startine - endtime);
        }
        return alltime / (size + 1);

    }

    public static double measureDeletions(AbstractSkipList skipList, int size) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public static void main(String[] args) {
        System.exit(1); // Remove this line.
    }
}
