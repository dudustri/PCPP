package exercises07;

// JUnit testing imports
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;

// Data structures imports
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

// Concurrency imports
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import exercises07.PrimeFactorTask;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class TestHistograms {
    private CasHistogram h1Cas; // our CASHistogram
    private Histogram2 h1Seq; // our old Histogram2 (thread-safe w. intrinsic locks)
    private PrimeFactorTask pftCas; // a prime-factor task for the CASHistogram
    private PrimeFactorTask pftSeq; // a prrime-factor task for Histogram2
    private static final int RANGE = 4_999_999; // range we want to count prime factors in

    @BeforeEach
    public void initialize(){
        h1Cas = new CasHistogram(25); // 25 bins
        h1Seq = new Histogram2(25); // 25 bins
        pftCas = new PrimeFactorTask(h1Cas, 0, RANGE); // primefactor task starting at 0
        pftSeq = new PrimeFactorTask(h1Seq, 0, RANGE); // primefactor task starting at 0

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 8, 16}) //2^n where n is in {0,1,2,3,4}
    public void testParallelCasHistogram(int numThreads){ //test will be run with 1,2,4,8,16 threads for CASHistogram
        int[] casResult = parallelTest(numThreads); //run the parallel test
        int[] seqResult = sequentialTest(); // run the sequential test

        assertArrayEquals(seqResult, casResult); // we compare both arrays, if equal -> success
    }

    private int[] parallelTest(int numThreads){
        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads); // a pool of threads
        forkJoinPool.invoke(pftCas); // perform the primefactor task with numThreads
        forkJoinPool.shutdown(); // no more tasks 
        System.out.println("CasHistogram");
        pftCas.dump(h1Cas); // print the histogram
        return pftCas.createArray(h1Cas); // create a an array of the bins for comparison
        // NOTE: we could simply have added a 'getBins' to Histogram interface
        // but we didn't whether that was allowed.
    }


    //FEEDBACK: It is wrong to to invoke() before shutdown()
    // We adding tasks recursively, 
    // invoke() would return futures but we should on the threads

    private int[] sequentialTest(){
        pftSeq.compute();// perform the primefactor task
        System.out.println("Histogram2");
        pftSeq.dump(h1Seq); // print the histogram
        return pftSeq.createArray(h1Seq); // create a an array of the bins for comparison
        // NOTE: we could simply have added a 'getBins' to Histogram interface
        // but we didn't whether that was allowed.
    }
}

/*
 * 1. functional correctness
 * 2. Parallel (multiple threads)
 * 3. Correctly stores the number of prime of factors in range 0..4999999
 * 4. Test must be with 1 to 16 threads
 * 5. Perform the same computation using a different Histogram1 sequentially.
 * 6. Check that each bin from CasHistogram equals the same bin for Histogram1
 * 
 */
