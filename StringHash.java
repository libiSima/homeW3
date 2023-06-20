import java.util.Random;

public class StringHash implements HashFactory<String> {

    public StringHash() {
        ;
    }

    @Override
    public HashFunctor<String> pickHash(int k) {
        return new Functor(new ModularHash().pickHash(k));
    }

    public class Functor implements HashFunctor<String> {
        final private HashFunctor<Integer> carterWegmanHash;
        final private int c;
        final private int q;

        @Override
        public int hash(String key) {
        	int sum = 0;
        	int k = key.length();
        	for (int i = 0; i < k; i++)
        	{
        		sum = (int)HashingUtils.fastModularPower( sum + (int)HashingUtils.fastModularPower( ((int)key.charAt(i)) * (int)HashingUtils.fastModularPower(c, k - i, q), (long)1, (long)q),(long)1 ,(long)q);
        	}
        	return carterWegmanHash.hash(Math.abs(sum));
        }
        
        public Functor(HashFunctor<Integer> carterWegmanHash)
        {
        	this.carterWegmanHash = carterWegmanHash;
        	int qPossible = new HashingUtils().genUniqueIntegers(1)[0];
        	while ( qPossible <= Integer.MAX_VALUE/2 || !( new HashingUtils().runMillerRabinTest(qPossible, 13) ) )
        		qPossible = new HashingUtils().genUniqueIntegers(1)[0];
        	q = qPossible;
        	c = new Random().nextInt(2, q);
        }

        public int c() {
            return c;
        }

        public int q() {
            return q;
        }

        public HashFunctor carterWegmanHash() {
            return carterWegmanHash;
        }
    }
}
