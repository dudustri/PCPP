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
import java.util.Collections;

// Concurrency imports
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestLocks {

    // Assuming ReadWriteCASLock is your lock class
    private ReadWriteCASLock lock = new ReadWriteCASLock();
    private AtomicInteger concurrentWriters = new AtomicInteger(0);
    private volatile boolean errorDetected = false;

    @Test
    @RepeatedTest(100)
    @Disabled
    public void twoWritersCannotAcquireLockSimultaneously() throws InterruptedException {
        int numberOfThreads = 10; // number of threads to run concurrently
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads); // create thread pool

        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(this::writerTask); // submit writer task to thread pool
        }
        
     
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);

        // Check that there was never a situation where two threads had the lock simultaneously
        Assertions.assertFalse(errorDetected, "Two writers acquired the lock at the same time!");
    }

    private void writerTask() {
        try {
            if (lock.writerTryLock()) {
                int writers = concurrentWriters.incrementAndGet();

                // Check if another writer has the lock
                if (writers > 1) {
                    errorDetected = true;
                }

                // Simulating some work
                Thread.sleep(100);

                concurrentWriters.decrementAndGet();
                lock.writerUnlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testCannotTakeReadLockWhenHoldingWriteLock() {
        ReadWriteCASLock lock = new ReadWriteCASLock();
        assertTrue(lock.writerTryLock()); // Acquire write lock

        // Trying to acquire read lock while holding write lock
        assertFalse(lock.readerTryLock());

        lock.writerUnlock();
    }

    @Test
    public void testCannotTakeWriteLockWhenHoldingReadLock() {
        ReadWriteCASLock lock = new ReadWriteCASLock();
        assertTrue(lock.readerTryLock()); // Acquire read lock

        // Trying to acquire write lock while holding read lock
        assertFalse(lock.writerTryLock());

        lock.readerUnlock();
    }

    @Test
    public void testCannotUnlockReadLockNotHeld() {
        ReadWriteCASLock lock = new ReadWriteCASLock();
        // Try unlocking read lock when not held
        assertThrows(IllegalStateException.class, lock::readerUnlock);
    }

    @Test
    public void testCannotUnlockWriteLockNotHeld() {
        ReadWriteCASLock lock = new ReadWriteCASLock();
        // Try unlocking write lock when not held
        assertThrows(IllegalStateException.class, lock::writerUnlock);
    }
    // Add other tests here to increase confidence

}



