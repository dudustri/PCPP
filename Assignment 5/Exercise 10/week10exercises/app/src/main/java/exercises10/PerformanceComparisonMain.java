package exercises10;
import java.util.ArrayList;
import java.util.List;
public class PerformanceComparisonMain {
	public static void main(String[] args) {
		
		long currentTime=System.currentTimeMillis();//just a long for time reference

		List<Integer> data=new ArrayList<Integer>();
		for (int i = 0; i < 100000; i++) {
			data.add(i);
		}//we make a ArrayList of 100000 ints

		
		long sum=data.stream()
                .parallel()
				.map(i ->(int)Math.sqrt(i))//square root of every int in the list
				.map(number->calPrimeFactors(number)) //do the expensive computation on every element in the list
				.reduce(0,Integer::sum); //reduce
		
		System.out.println(sum);
		long endTime=System.currentTimeMillis();
		System.out.println("Time taken to complete:"+(endTime-currentTime)/(1000)+" seconds");
		
	}
	
	public static int calPrimeFactors(int number)
	{
		int primes=0;
		for (int i = 1; i < 10000; i++) {
            if(isPrime(i)) primes++;
		}
		return primes;
	}

    private static boolean isPrime(int n) {
        int k= 2;
        while (k * k <= n && n % k != 0)
          k++;
        return n >= 2 && k * k > n;
      }
}
