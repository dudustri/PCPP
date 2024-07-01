package exercise62;

// Counting primes, using multiple threads for better performance.
// (Much simplified from CountprimesMany.java)
// sestoft@itu.dk * 2014-08-31, 2015-09-15
// modified rikj@itu.dk 2017-09-20
// modified jst@itu.dk 2021-09-24
// raup@itu.dk * 05/10/2022
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import benchmarking.Benchmark;

public class TestCountPrimesThreads {
    public static void main(String[] args) {
        new TestCountPrimesThreads();
    }

    public TestCountPrimesThreads() {
        final int range = 100_000;
        Benchmark.Mark7("countSequential", i -> countSequential(range));
        for (int c = 1; c <= 32; c++) {
            final int threadCount = c;
            Benchmark.Mark7(String.format("\u001B[31m countParallelN %2d", threadCount),
                    i -> countParallelN(range, threadCount));
            Benchmark.Mark7(String.format("\u001B[33m countParallelNLocal %2d", threadCount),
                    i -> countParallelNLocal(range, threadCount));
            Benchmark.Mark7(String.format("\u001B[32m countParallelNWithFutures %2d", threadCount),
                    i -> countParallelNWithFutures(range, threadCount));
            Benchmark.Mark7(String.format("\u001B[35m countParallelNLocalWithFutures %2d", threadCount),
                    i -> countParallelNLocalWithFutures(range, threadCount));
        }
    }

    private static boolean isPrime(int n) {
        int k = 2;
        while (k * k <= n && n % k != 0)
            k++;
        return n >= 2 && k * k > n;
    }

    // Sequential solution
    private static long countSequential(int range) {
        long count = 0;
        final int from = 0, to = range;
        for (int i = from; i < to; i++)
            if (isPrime(i))
                count++;
        return count;
    }

    // General parallel solution, using multiple threads
    private static long countParallelN(int range, int threadCount) {
        final int perThread = range / threadCount;
        final AtomicLong lc = new AtomicLong(0);
        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            final int from = perThread * t,
                    to = (t + 1 == threadCount) ? range : perThread * (t + 1);
            threads[t] = new Thread(() -> {
                for (int i = from; i < to; i++)
                    if (isPrime(i))
                        lc.incrementAndGet();
            });
        }
        for (int t = 0; t < threadCount; t++)
            threads[t].start();
        try {
            for (int t = 0; t < threadCount; t++)
                threads[t].join();
            // System.out.println("Primes: "+lc.get());
        } catch (InterruptedException exn) {
        }
        return lc.get();
    }

    // General parallel solution, using multiple threads
    private static long countParallelNLocal(int range, int threadCount) {
        final int perThread = range / threadCount; //the amount of work each thread will do
        final long[] results = new long[threadCount]; //results
        Thread[] threads = new Thread[threadCount]; //threads
        for (int t = 0; t < threadCount; t++) {
            final int from = perThread * t,//the start of the interval in which the thread will work
                    to = (t + 1 == threadCount) ? range : perThread * (t + 1);//the end of the interval
            final int threadNo = t;//the place to store the result in 'results'
            threads[t] = new Thread(() -> {//init a new thread
                long count = 0;
                for (int i = from; i < to; i++)
                    if (isPrime(i))
                        count++;
                results[threadNo] = count; //store result from given thread
            });
        }
        for (int t = 0; t < threadCount; t++)
            threads[t].start(); //start the threads
        try {
            for (int t = 0; t < threadCount; t++)
                threads[t].join();
        } catch (InterruptedException exn) {
        }
        long result = 0;
        for (int t = 0; t < threadCount; t++)
            result += results[t]; //sum the results
        return result;
    }


    /******************************************************************************
      Implementation of Parallel atomic long with Future and ThreadPool - ForkJoin
    *******************************************************************************/
    
    private static long countParallelNWithFutures(int range, int threadCount) {
        final int perThread = range / threadCount; //the amount of work each thread will do
        final AtomicLong lc = new AtomicLong(0); //a shared counter

        ForkJoinPool pool = new ForkJoinPool(threadCount); //we create the pool
        List<Future<Void>> futures = new ArrayList<>(); //and a list of Futures
 
        for (int t = 0; t < threadCount; t++) {
            final int from = perThread * t, to = (t + 1 == threadCount) ? range : perThread * (t + 1);  //define the work interval
            futures.add(pool.submit(() -> { //we add each thread and task to the list of futures
                for (int i = from; i < to; i++)
                    if (isPrime(i)) 
                        lc.incrementAndGet(); //if prime, we increment the shared counter
                return null;
            }));
        }

        futures.forEach(done -> {
            try {
                done.get(); //for each future in the list, we the retrieve the result
            } catch (InterruptedException | ExecutionException exn) {
                exn.printStackTrace();
            }
        });

        pool.shutdown(); //execute all task in the pool and accept no more

        return lc.get(); //get the final result
    }

    // Implementation of Parallel Local with Future and ThreadPool
    private static long countParallelNLocalWithFutures(int range, int threadCount) {
        final int perThread = range / threadCount; //amount of work each thread will do 
        final long[] results = new long[threadCount]; //list of results for each thread

        ForkJoinPool pool = new ForkJoinPool(threadCount); //we create a pool
        List<Future<?>> futures = new ArrayList<>(); // and a list of futures

        for (int t = 0; t < threadCount; t++) {
            final int from = perThread * t, to = (t + 1 == threadCount) ? range : perThread * (t + 1); //start- and end-interval
            final int threadNo = t; //thrad number
            futures.add(pool.submit(() -> { //we add each task to the pool and to the list of futures
                long count = 0; //local counter
                for (int i = from; i < to; i++)
                    if (isPrime(i)) 
                        count++; //if prime, we increment
                results[threadNo] = count; //add the local counter the results array for the given thread
                return null;
            }));
        }

        futures.forEach(done -> {
            try {
                done.get(); //we get the result for each thread
            } catch (InterruptedException | ExecutionException exn) {
                exn.printStackTrace();
            }
        });
 
        pool.shutdown(); //shut down the pool

        long result = 0;
        for (int t = 0; t < threadCount; t++)
            result += results[t]; //collect the final result from the result array
        return result; //return the final result
    }
}