package exercises07;


import java.util.concurrent.ForkJoinPool;

public class TestHistograms {
    private final CasHistogram h1Cas;
    private final Histogram1 h1Seq;
    private final Histogram2 h2;
    private final PrimeFactorTask pftCas;
    private final PrimeFactorTask pftSeq;
    private static final int RANGE = 4_999_999;

    public TestHistograms(){
        h1Cas = new CasHistogram(25);
        h2 = new Histogram2(25);
        h1Seq = new Histogram1(25);
        pftCas = new PrimeFactorTask(h1Cas, 0, RANGE);
        pftSeq = new PrimeFactorTask(h1Seq, 0, RANGE);
    }

    public static void main(String[] args) {
        TestHistograms t1 = new TestHistograms();
        t1.testParallelCasHistogram(2);
    }

    public void testParallelCasHistogram(int numThreads){
        int[] casResult = parallelTest(numThreads);
        int[] seqResult = sequentialTest();
    }

    private int[] parallelTest(int numThreads){
        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads);
        forkJoinPool.invoke(pftCas);
        forkJoinPool.shutdown();
        // forkJoinPool.close();
        pftCas.dump(h1Cas);
        return pftCas.createArray(h1Cas);
    }

    private int[] sequentialTest(){
        PrimeFactorTask seqFactorTask = new PrimeFactorTask(h2, 0, RANGE);
        seqFactorTask.compute();
        pftSeq.dump(h1Seq);
        return pftSeq.createArray(h1Seq);

    }
}