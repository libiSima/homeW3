import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    public MultiplicativeShiftingHash() {

    }

    @Override
    public HashFunctor<Long> pickHash(int k) {
        HashingUtils hashu = new HashingUtils();
        long a = hashu.genLong(1, Integer.MAX_VALUE);
        Functor fuctor = new Functor(a, k);
        return fuctor;
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(long a, long k) {
            this.a = a;
            this.k = k;
        }

        @Override
        public int hash(Long key) {
            int ans = (int) ((a * key) >>> (64 - k));
            return ans;
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
