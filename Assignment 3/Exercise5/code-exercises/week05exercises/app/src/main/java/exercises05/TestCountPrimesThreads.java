package exercises05;
// Counting primes, using multiple threads for better performance.
// sestoft@itu.dk * 2014-08-31, 2015-09-15
// modified jst@itu.dk 2023-09-05

import java.util.concurrent.atomic.AtomicLong;

import benchmarking.Benchmark;
import benchmarking.Benchmarkable;

public class TestCountPrimesThreads {

  public static void main(String[] args) { new TestCountPrimesThreads(); }

  public TestCountPrimesThreads() {
    Benchmark.SystemInfo();
    final int range= 100_000;
    Benchmark.Mark7("countSequential", i -> countSequential(range));
    for (int c= 1; c<=16; c++) {
      final int threadCount = c;
      Benchmark.Mark7(String.format("countParallelN %7d", threadCount), 
            i -> countParallelN(range, threadCount));
      //Benchmark.Mark7(String.format("countParallelNLocal %2d", threadCount), 
      //      i -> countParallelNLocal(range, threadCount));
    }
  }

  private static boolean isPrime(int n) {
    int k = 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

  // Sequential solution
  private static int countSequential(int range) {
    int count= 0;
    final int from= 0, to= range;
    for (int i= from; i<to; i++)
      if (isPrime(i)) count++;
    return count;
  }

  // General parallel solution, using multiple threads
  private static int countParallelN(int range, int threadCount) {
    final int perThread= range / threadCount; //we calculate the amount of work each thread should do
    AtomicLong counter = new AtomicLong(); //we create an AtomicLong as a shared counter 
    Thread[] threads= new Thread[threadCount];//a collection for our threads
    for (int t= 0; t<threadCount; t++) {
        final int from= perThread * t, //we define the start-interval for the thread
        to = (t+1==threadCount) ? range : perThread * (t+1);  //and the end-interval for the given thread
        threads[t]= new Thread( () -> { //initiate the thread
          int j = 0; //local counter variable (requirement)
          for (int i= from; i<to; i++){
            if (isPrime(i)) j++;//if prime, we increment j
          }
          counter.addAndGet(j); //we add the amount of primes the given thread thread in its interval
        });
    }
    for (int t= 0; t<threadCount; t++) 
      threads[t].start(); //start the threads
    try {
      for (int t=0; t<threadCount; t++) 
        threads[t].join(); //join the threads
        //System.out.println("Primes: "+lc.get());
    } catch (InterruptedException exn) { }
    return counter.intValue();
  }

  // General parallel solution, using multiple threads
  private static int countParallelNLocal(int range, int threadCount) {
    //...
    return 0; //change 0 to result;
  }
}


