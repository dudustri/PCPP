package exercises05;

import benchmarking.Benchmark;

/*I am unsure whether we need create multiple threads and such
but from reading the description of the assignment, it
seems to be just the implementation below*/

public class TestVolatile {
    private volatile int vCtr;
    private int ctr;

    public static void main(String[] args) {
        new TestVolatile().runBenchmark();
    }

    public void runBenchmark() {
        Benchmark.SystemInfo();

        Benchmark.Mark7("incrementVolatile", i -> {
            vInc();
            return i;
        });
        
        Benchmark.Mark7("incrementNonVolatile", i -> {
            inc();
            return i;
        });
    }

    public void vInc () {
        vCtr++;
    }
    public void inc () {
        ctr++;
    } 
}