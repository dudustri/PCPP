package exercises07;

import java.util.concurrent.RecursiveAction;

public class PrimeFactorTask extends RecursiveAction  { //we use 'RecursiveAction' because 'compute' does not have a return type
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
            if (end - start < THRESHOLD) {
                for (int i = start; i <= end; i++) {
                    histogram.increment(countFactors(i));
                }
            } else {
                int middle = (start + end) / 2;
                PrimeFactorTask leftTask = new PrimeFactorTask(histogram, start, middle);
                PrimeFactorTask rightTask = new PrimeFactorTask(histogram, middle + 1, end);
                invokeAll(leftTask, rightTask);
            }
        }

    // Returns the number of prime factors of `p`
    public int countFactors(int p) {
        if (p < 2) {
            return 0;
        }
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

    public void dump(Histogram histogram) {
        for (int bin= 0; bin < histogram.getSpan(); bin= bin+1) {
          System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
        }
    }

    public int[] createArray(Histogram histogram){
        int[] histoArray = new int[histogram.getSpan()];
        for(int i = 0; i < histogram.getSpan(); i = i + 1){
            histoArray[i] = histogram.getCount(i);
        }
        return histoArray;
    }
}