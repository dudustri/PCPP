package exercises04;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;


public class ConcurrentSetTest {

    // Variable with set under test
    private ConcurrentIntegerSet set;
    private CyclicBarrier barrier;
        

    // Uncomment the appropriate line below to choose the class to test
    // Remember that @BeforeEach is executed before each test
    @BeforeEach
    public void initialize() {
		// ---- initializing the set to be tested ----
		set = new ConcurrentIntegerSetBuggy();
		//set = new ConcurrentIntegerSetSync();	
    //set = new ConcurrentIntegerSetLibrary();
    }


    // ----------------------------------------------------------------
    //After N-many add() operations, the set should contain N-many integers.
    @RepeatedTest(500) 
    @DisplayName ("Add Test")
    @Disabled
    public void testConcurrentIntegerSetAdd (){
      System.out.println("test concurrent integer add");
      final int nrThreads = 16; //amount of threads
      final int N = 50;  //add operations per thread

      //we create CyclicBarrier with n+1 threads where the + 1 is the testing thread!
      barrier = new CyclicBarrier(nrThreads+1);

      //we initialize all threads 
      for (int i = 0; i < nrThreads; i++) {
  
          new Thread(() -> {
            try {
              barrier.await(); //wait for all threads ready to start
              for (int j = 0; j < N; j++) {
                set.add(j); //for every thread (16), we add 0-49 to the set 
              } 
              barrier.await(); //wait for all threads ready to finish their respective add operations
            } catch (InterruptedException | BrokenBarrierException e) {
              e.printStackTrace();
            } 
          }).start();
      }

      try { 
        barrier.await(); // wait until threads are ready for execution (maximize contention)
        barrier.await(); // wait for threads to finish
      } catch (InterruptedException | BrokenBarrierException e) { 
        e.printStackTrace(); 
      }

      assertTrue(N == set.size(), "set.size() is " + set.size() + " but we expected " + N);
      //[Q]: When we do tests, do we care about performance?
      //Is it not better to have a slow test that tests for edge cases?
    }

    //----------------------------------------------------------------
    //After N-many add operations and subsequent remove, we expect the size to be 0
    @RepeatedTest(500) 
    @DisplayName ("Remove Test")
    @Disabled
    public void testConcurrentIntegerSetRemove(){
      System.out.println("test concurrent integer remove");
      final int nrThreads = 16;
      final int N = 50;

      //instanciating the CyclicBarrier with n+1 threads where the + 1 is the testing thread!
      barrier = new CyclicBarrier(nrThreads+1);

      // adding elements to the set
      for (int j = 0; j < N; j++) {
        set.add(j); //add 0-49 to the set
      } 

      //initialize all threads 
      for (int i = 0; i < nrThreads; i++) {
  
          new Thread(() -> {
            try {
              barrier.await(); //wait for all threads ready to start
              for (int j = N-1; j >= 0; j--) {
                set.remove(j); //every thread tries to remove 0-49 from the set
              } 
              barrier.await(); //wait for all threads to finish their respective remove operations
            } catch (InterruptedException | BrokenBarrierException e) {
              e.printStackTrace();
            } 
          }).start();
      }

      try { 
        barrier.await(); // wait until threads are ready for execution (maximize contention)
        barrier.await(); // wait for threads to finish
      } catch (InterruptedException | BrokenBarrierException e) { 
        e.printStackTrace(); 
      }

      assertTrue(set.size() == 0, "set.size() is " + set.size() + " but we expected 0");
    }


    /* TEST BELOW IS KEPT BEACUSE WE QUESTIONS (the implementation is not correct) */
    //After N-many remove() operations, the set should contain N-many integers.
    @RepeatedTest(500)
    @DisplayName ("Remove Test Bug")
    //@Disabled
    public void testConcurrentIntegerSetBuggyBugRemove() {
        System.out.println("test concurrent integer remove - implementation bug when running SetBuggy");
        final int nrThreads = 10;
        final int N = 50;

        barrier = new CyclicBarrier(nrThreads + 1);
      
        set.add(1);

        for (int i = 0; i < nrThreads; i++) {
            new Thread(() -> {
                try {
                    barrier.await(); // Wait for all threads ready to start
                    for (int j = 0; j < N; j++) {

                        set.remove(1);
                        int size = set.size();
                        if (size < 0) {
                            assertTrue(false, "size is negative :(");
                        }
                        set.add(1);
                    }

                    //barrier.await(); 
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        try {
           // barrier.await(); // Comment this barrier.await() to run ConcurrentIntegerSetBuggy Test otherwise it runs forever.
            barrier.await(); 
            //[Q] Why if we add a barrier.wait() here for the ConcurrentIntegerSetBuggy test it runs forever? 
            //[Q] Is it a deadlock? We couldn't find the problem. Probably with the thread manager and the barrier, right?
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        assertTrue(set.size() == 1, "set.size() is " + set.size() + " but we expected 1");
    }
}

