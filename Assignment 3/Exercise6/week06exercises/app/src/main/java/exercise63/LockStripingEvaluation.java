package exercise63;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.IntToDoubleFunction;
import benchmarking.Benchmark;

import static exercise63.HistogramPrimesThreads.countFactors;

public class LockStripingEvaluation {

    private static final int RANGE = 4_999_999; //range
    private static final int THRESHOLD = 10_000; // Adjust as needed

    public static void main(String[] args) {
        for (int i = 1; i <= 20; i *= 2) {
            final int nrLocks = i;
            Benchmark.Mark7(String.format("Histogram3 with %d locks", nrLocks), index -> {
                return measurePerformanceWithLocks(nrLocks);
            });
        }
    }

    public static double measurePerformanceWithLocks(int nrLocks) {
        Histogram histogram = new Histogram3(25, nrLocks); //histogram 3, 25 bins, and locks as needed
        ForkJoinPool forkJoinPool = new ForkJoinPool(); //we create a forkjoin pool
        long startTime = System.nanoTime(); //start time

        forkJoinPool.invoke(new CountFactorsTask(histogram, 0, RANGE)); //count the factors

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1e6;
    }

    static class CountFactorsTask extends RecursiveAction { // Note the 'static' modifier
        private final Histogram histogram;
        private final int start;
        private final int end;

        CountFactorsTask(Histogram histogram, int start, int end) {
            this.histogram = histogram;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start < THRESHOLD) {
                for (int i = start; i <= end; i++) {
                    histogram.increment(countFactors(i));
                }
            } else {
                int middle = (start + end) / 2;
                CountFactorsTask leftTask = new CountFactorsTask(histogram, start, middle);
                CountFactorsTask rightTask = new CountFactorsTask(histogram, middle + 1, end);
                invokeAll(leftTask, rightTask);
            }
        }
    }
}
