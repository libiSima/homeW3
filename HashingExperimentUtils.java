import java.util.Collections; // can be useful

public class HashingExperimentUtils {
    final private static int k = 16;
    public static Pair<Double, Double> measureOperationsChained(double maxLoadFactor) {
        ModularHash hash = new ModularHash();
    	ChainedHashTable<Integer, Integer> htable = new ChainedHashTable<Integer, Integer>(hash, k, maxLoadFactor);
    	Double sumInsert = 0.0;
    	Double sumSearch = 0.0;
    	Double twoToSixteen = (1 << 16)*1.0;
    	Integer[] toInsAndCheck = new HashingUtils().genUniqueIntegers(2*(int)(twoToSixteen*maxLoadFactor));
    	for (int i = 0; i < twoToSixteen*maxLoadFactor; i++)
    	{
    		Long toAdd = System.nanoTime();
    		htable.insert(toInsAndCheck[i], i);
    		toAdd -= System.nanoTime();
    		sumInsert += Math.abs(toAdd)/(twoToSixteen*maxLoadFactor);
    	}
    	for (int i = 0; i < 2*(int)(twoToSixteen*maxLoadFactor); i++)
    	{
    		Long toAdd = System.nanoTime();
    		htable.search(toInsAndCheck[i]);
    		toAdd -= System.nanoTime();
    		sumSearch += Math.abs(toAdd)/twoToSixteen;
    	}
    	//Test
    	return new Pair<Double, Double>(sumInsert, sumSearch);

    }

    public static Pair<Double, Double> measureOperationsProbing(double maxLoadFactor) {
        ModularHash hash = new ModularHash();
    	ProbingHashTable<Integer, Integer> htable = new ProbingHashTable<Integer, Integer>(hash, 16, maxLoadFactor);
    	Double sumInsert = 0.0;
    	Double sumSearch = 0.0;
    	Double twoToSixteen = (1 << 16)*1.0;
    	Integer[] toInsAndCheck = new HashingUtils().genUniqueIntegers(2*(int)(twoToSixteen*maxLoadFactor));
    	for (int i = 0; i < twoToSixteen*maxLoadFactor; i++)
    	{
    		Long toAdd = System.nanoTime();
    		htable.insert(toInsAndCheck[i], i);
    		toAdd -= System.nanoTime();
    		sumInsert += Math.abs(toAdd)/(twoToSixteen*maxLoadFactor);
    	}
    	for (int i = 0; i < 2*(int)(twoToSixteen*maxLoadFactor); i++)
    	{
    		Long toAdd = System.nanoTime();
    		htable.search(toInsAndCheck[i]);
    		toAdd -= System.nanoTime();
    		sumSearch += Math.abs(toAdd)/twoToSixteen;
    	}
    	//Test
    	return new Pair<Double, Double>(sumInsert, sumSearch);
    }

    public static Pair<Double, Double> measureLongOperations() {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public static Pair<Double, Double> measureStringOperations() {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public static void main(String[] args) {
    	double[] probs = new double[] {0.5,0.75,0.875,0.9375};
    	
    	/*
    	for (int i = 0; i < 30; i++) {
    		for (double pr: probs)
    		{
    			Pair<Double, Double> p = measureOperationsProbing(pr);
    			System.out.println("i = "+i+". p = "+pr+": "+p.first()+","+p.second());
    			averageTimes[0] += p.first()/30;
    			averageTimes[1] += p.second()/30;
    		}
    	}
    	*/
    	probs = new double[] {0.5,0.75,1,1.5,2};
    	double[][] averageTimes = new double[probs.length][];
    	for (int pr = 0; pr < probs.length; pr++)
    		averageTimes[pr]= new double[] {0,0};
    	System.out.println("Chained:");
    	for (int i = 0; i < 30; i++) {
    		for (int pr = 0; pr < probs.length; pr++)
    		{
    			Pair<Double, Double> p = measureOperationsChained(probs[pr]);
    			averageTimes[pr][0] += p.first()/30;
    			averageTimes[pr][1] += p.second()/30;
    		}
    	}
    	for (int pr = 0; pr < probs.length; pr++) {
    		System.out.println("			 \\hline\n			 "+"$"+probs[pr]+"$ & "+averageTimes[pr][0]+" & "+averageTimes[pr][1]+ (" \\\\") );
    	}
    	System.out.println("			 \\hline\n");
        System.exit(1); // Remove this line
    }
}
