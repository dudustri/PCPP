package exercises04;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

public class SemaphoreImpTest{

    private SemaphoreImp semaphore;
    private final int cap = 1; //we set capacity to 1
    private final int nrThreads = 8; //and threads to 8

    private CyclicBarrier barrier; //we create a barrier to make threads wait each other

    @BeforeEach
    public void initialize() {
        semaphore = new SemaphoreImp(cap);
    }

    @Test
    @DisplayName("Semaphore Capacity Violation Test")
    public void semaphoreCapacityViolationTest() throws InterruptedException {
        barrier = new CyclicBarrier(nrThreads+1);//we initialize the barrier to account for the main thread

        final AtomicInteger acquireCounter = new AtomicInteger(0);
        //we define counter variable
        
        semaphore.acquire();
        //acquire the max capacity of the semaphore (1)
        acquireCounter.incrementAndGet();
        //Initialize and start eight threads organized in a array
        Thread[] threads = new Thread[nrThreads];
        for (int i = 0; i < nrThreads; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    // Commented the line to make the test pass (to exploit and enter in the critical section uncommented below)
                    semaphore.release();
                    semaphore.acquire();
                    //Count when a thread enters in the critical section
                    acquireCounter.incrementAndGet();
                    // We are not using barrier.await() here since it would create a deadlock when running the test
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        try {
            barrier.await();
            // wait 3 seconds to make sure that all threads were created and started
            Thread.sleep(3000);
            // We can not use barrier.await() here because it will block the test forever when using the exploitation 
            // Deadlock -> (threads in the WAITING State)
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        // Check the state of all threads if they are waiting to in the acquire condition
        // When calling the release before acquire the thread will have a TERMINATED flag/state and the test will fail
        for (Thread thread : threads) {
            assertEquals(Thread.State.WAITING, thread.getState(), "Thread entered the critical section instead of waiting -> Actual state: " + thread.getState());
        }
        
        //double check - assertion to check the number of threads running in the critical section
        assertTrue(acquireCounter.get() == 1, "The total threads running in the critical section should be equal to the capacity " + cap + "but it is " + acquireCounter.get());
    }
}
