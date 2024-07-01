package exercise63;

// first version by Kasper modified by jst@itu.dk 24-09-2021
// raup@itu.dk * 05/10/2022
// jst@itu.dk 22-09-2023

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class HistogramPrimesThreads {
    private static final int RANGE = 4_999_999; // constant for which the number of prime factors will be calculated

    public static void main(String[] args) { new HistogramPrimesThreads(); }

    public HistogramPrimesThreads() {
        final Histogram histogram = new Histogram2(25); // 25 bins for the histogram

        ForkJoinPool forkJoinPool = new ForkJoinPool(); //we create a pool
        forkJoinPool.invoke(new PrimeFactorTask(histogram, 0, RANGE)); // invoke the task and the Range
        forkJoinPool.shutdown(); //no more tasks, execute the tasks in the pool

        dump(histogram); //print
    }

    static class PrimeFactorTask extends RecursiveAction  { //we use 'RecursiveAction' because 'compute' does not have a return type
        private final Histogram histogram;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 10_000; // Adjust if needed

        PrimeFactorTask(Histogram histogram, int start, int end) {
            this.histogram = histogram;
            this.start = start;
            this.end = end;
        }

        
        protected void compute() {
            if (end - start < THRESHOLD) { //if the threshold is met, we calculate the amount of primes for the given interval
                for (int i = start; i <= end; i++) {
                    histogram.increment(countFactors(i)); //we add the amount of prime factors for the given bin in the histogram
                }
            } else { //if not, we divide the histogram into a right and left part (similar to mergeSort)
                int middle = (start + end) / 2;
                PrimeFactorTask leftTask = new PrimeFactorTask(histogram, start, middle);
                PrimeFactorTask rightTask = new PrimeFactorTask(histogram, middle + 1, end);
                invokeAll(leftTask, rightTask); //repeat the process
            }
        }
    }

    // Returns the number of prime factors of `p`
    public static int countFactors(int p) {
      if (p < 2) return 0;
      int factorCount = 1, k = 2;
      while (p >= k * k) {
        if (p % k == 0) {
          factorCount++;
          p= p/k;
        } else 
          k= k+1;
      }
      return factorCount;
    }

    public static void dump(Histogram histogram) {
      for (int bin= 0; bin < histogram.getSpan(); bin= bin+1) {
        System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
    }
  }
}

