import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    public ModularHash() {
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        return new Functor(1<<k);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;
        @Override
        public int hash(Integer key) {
        	return (int)HashingUtils.fastModularPower(HashingUtils.fastModularPower( (((long)a)*((long)key)+(long)b) ,(long)1,p),(long)1,(long)m);
        }
        
        public int hash(String str) {
        	int sum = 0;
        	for (int i = 0; i < str.length(); i++)
        	{
        		sum = (int)HashingUtils.fastModularPower((int)str.charAt(i) + HashingUtils.fastModularPower(256, i, Integer.MAX_VALUE), (long)1, (long)Integer.MAX_VALUE);
        	}
        	return hash(sum);
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
        
        private Functor(int a,int b,int p,int m)
        {
        	this.a = a;
        	this.b = b;
        	this.p = p;
        	this.m = m;
        }
        
        public Functor(int m)
        {
        	//ASK ABOUT STATIC FUNTIONS IN HASHINGUTILS OR CONSTRUCTOR FUNCTION
        	HashingUtils hashingUtils = new HashingUtils();
        	Integer[] arr = hashingUtils.genUniqueIntegers(2);
        	Long[] longPrime = hashingUtils.genUniqueLong(1);
        	while (!isProperLongPrime(longPrime[0], hashingUtils))
        		longPrime = hashingUtils.genUniqueLong(1);
        	this.a = arr[0];
        	this.b = arr[1];
        	this.p = (long)longPrime[0];
        	this.m = m;
        }
        
        private boolean isProperLongPrime(long l,HashingUtils hashingUtils)
        {
        	return (l>Integer.MAX_VALUE) && (hashingUtils.runMillerRabinTest(l, 13));
        }
        
    }
}
