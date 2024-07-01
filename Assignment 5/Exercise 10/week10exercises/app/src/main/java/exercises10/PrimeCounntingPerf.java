package exercises10;
//Exercise 10.1
//JSt vers Oct 23, 2023

import java.util.*;
import java.util.stream.*;
import benchmarking.Benchmark;

class PrimeCountingPerf { 
  public static void main(String[] args) { new PrimeCountingPerf(); }
  static final int range= 100000;

  //Test whether n is a prime number
  private static boolean isPrime(int n) {
    int k= 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

// Sequential solution
  private static long countSequential(int range) {
    long count = 0;
    final int from = 0, to = range;
    for (int i=from; i<to; i++)
      if (isPrime(i)) count++;
    return count;
  }

  // IntStream solution
  private static long countIntStream(int range) {
    long count= 0;
    count =
      IntStream.range(2, range) // we define an IntStream in the given range.
      .filter(i -> isPrime(i)) // next, we filter out all of the numbers within the range which are not primes
      //.forEachOrdered(System.out::println); // we add this to print every prime within the range 0..1000
      .count(); // and count the remaining numbers 
    return count;
  }

  // Parallel Stream solution
  private static long countParallel(int range) {
    long count= 0;
    count = 
      IntStream.range(2, range) // we define an IntStream in the given range.
      .parallel() // we parllelize the stream
      .filter(i -> isPrime(i))
      //.forEachOrdered(System.out::println); // we add this to print every prime within the range 0..1000
      .count(); // -||-
    return count;
  }

// parallelStream solution
  private static long countparallelStream(List<Integer> list) {
    long count= 0;
    count = 
      list.parallelStream()
      .filter(i -> isPrime(i))
      //.forEachOrdered(System.out::println); // we add this to print every prime within the range 0..1000
      .count();
    return count;
  }

  public PrimeCountingPerf() {
    Benchmark.Mark7("Sequential", i -> countSequential(range));

    Benchmark.Mark7("IntStream", i -> countIntStream(range));
    
    Benchmark.Mark7("Parallel", i -> countParallel(range));

    List<Integer> list = new ArrayList<Integer>();
    for (int i= 2; i< range; i++){ list.add(i); }
    Benchmark.Mark7("ParallelStream", i -> countparallelStream(list));
  }
}


