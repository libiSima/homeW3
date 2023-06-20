import java.util.Collections; // can be useful
import java.util.List;

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
    	double maxLoadFactor = 1;
        HashFactory<Long> hash = new MultiplicativeShiftingHash();
    	ChainedHashTable<Long, Integer> htable = new ChainedHashTable<Long, Integer>(hash, 16, maxLoadFactor);
    	Double sumInsert = 0.0;
    	Double sumSearch = 0.0;
    	Double twoToSixteen = (1 << 16)*1.0;
    	Long[] toInsAndCheck = new HashingUtils().genUniqueLong((2*(int)(twoToSixteen*maxLoadFactor)));
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

    public static Pair<Double, Double> measureStringOperations() {
    	double maxLoadFactor = 1;
        HashFactory<String> hash = new StringHash();
    	ChainedHashTable<String, Integer> htable = new ChainedHashTable<String, Integer>(hash, 16, maxLoadFactor);
    	Double sumInsert = 0.0;
    	Double sumSearch = 0.0;
    	Double twoToSixteen = (1 << 16)*1.0;
    	List<String> ls = new HashingUtils().genUniqueStrings((2*(int)(twoToSixteen*maxLoadFactor)),10,20);
    	String[] toInsAndCheck = new String[ls.size()];
    	for (int i = 0 ; i < toInsAndCheck.length; i++) {
    		toInsAndCheck[i] = ls.get(i);
    	}
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

    public static void main(String[] args) {
    	double[] probs;
    	double[][] averageTimes;
    	
    	//STRING
    	System.out.println("String:");
    	averageTimes = new double[1][2];
    	for (int i = 0; i < 10; i++) {
    		Pair<Double, Double> p = measureStringOperations();
    		averageTimes[0][0] += p.first()/10;
    		averageTimes[0][1] += p.second()/10;
    	}
    	System.out.println("Average times for the string operations are: "+averageTimes[0][0]+" for the insertion operation and "+averageTimes[0][1]+" for the search operation.");

    	//LONG
    	System.out.println("Long:");
    	averageTimes = new double[1][2];
    	for (int i = 0; i < 10; i++) {
    		Pair<Double, Double> p = measureLongOperations();
    		averageTimes[0][0] += p.first()/10;
    		averageTimes[0][1] += p.second()/10;
    	}
    	System.out.println("Average times for the long operations are: "+averageTimes[0][0]+" for the insertion operation and "+averageTimes[0][1]+" for the search operation.");
    	
    	
    	//PROBING
    	probs = new double[] {0.5,0.75,0.875,0.9375};
    	averageTimes = new double[probs.length][];
    	for (int pr = 0; pr < probs.length; pr++)
    		averageTimes[pr]= new double[] {0,0};
    	System.out.println("Probing:");
    	for (int i = 0; i < 30; i++) {
    		for (int pr = 0; pr < probs.length; pr++)
    		{
    			Pair<Double, Double> p = measureOperationsProbing(probs[pr]);
    			System.out.println("i = "+i+", MaxLoadFactor = "+probs[pr]);
    			//System.out.println("i = "+i+",pr = "+probs[pr]+": "+p.first()+","+p.second());
    			averageTimes[pr][0] += p.first()/30;
    			averageTimes[pr][1] += p.second()/30;
    		}
    	}
    	for (int pr = 0; pr < probs.length; pr++) {
    		System.out.println("			 \\hline\n			 "+"$"+probs[pr]+"$ & "+averageTimes[pr][0]+" & "+averageTimes[pr][1]+ (" \\\\") );
    	}
    	System.out.println("			 \\hline\n");
    	
    	//CHAINED
    	probs = new double[] {0.5,0.75,1,1.5,2};
    	averageTimes = new double[probs.length][];
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
