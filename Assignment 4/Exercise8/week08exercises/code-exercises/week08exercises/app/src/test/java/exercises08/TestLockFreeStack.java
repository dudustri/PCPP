// raup@itu.dk * 2023-10-20 
package exercises08;

// JUnit testing imports

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class TestLockFreeStack {

    private LockFreeStack<Integer> stack;
    private AtomicInteger operationResult;
    private CyclicBarrier barrier;

    @BeforeEach
    void setUp() {
        stack = new LockFreeStack<>();
    }

    @ParameterizedTest
    @CsvSource({ "5, 200", "10, 100", "20, 50" }) // Define different thread and element counts
    void testPush(int threadsNumber, int pushesNumber) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber); // a pool for our threads
        int expectedResult = 1000; // the result we expect
        operationResult = new AtomicInteger(0); // an atomic integer which our threads will use
        barrier = new CyclicBarrier(threadsNumber);

        // For each thread, submit the task
        for (int i = 0; i < threadsNumber; i++) {
            executor.submit(() -> { // we submit the task below to the pool
                try {
                    // Wait for all threads to be created to start the execution together
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < pushesNumber; j++) { 
                    int value = (int)(Math.random() * 10); // we create random value 
                    stack.push(value); // push the random value into the stack
                    operationResult.addAndGet(1); // add 1 to the shared AtomicInteger
                }
            });
        }

        // Shutdown the executor and execute all tasks that have been submitted
        executor.shutdown();
        // Wait for all threads to finish their tasks
        while (!executor.isTerminated()) {
        }

        int totalPushed = 0; 
        Integer poppedValue;
        while ((poppedValue = stack.pop()) != null) { // we pop all values from the stack
            totalPushed += 1; // and add 1 to 'totalPushed' to keep track
        }
 
        assertEquals(operationResult.get(), expectedResult); // the shared AtomicInteger should be equal to expected result
        assertEquals(totalPushed, expectedResult); // same goes for 'totalpushed' 
        //-> the amount of values we pushed, should also be equal to the amount we popped
    }

    @ParameterizedTest
    @CsvSource({ "5, 200", "10, 100", "20, 50" }) // Define different thread and element counts
    void testPop(int threadsNumber, int elements) throws InterruptedException {
        AtomicInteger popOperations = new AtomicInteger(0); 
        barrier = new CyclicBarrier(threadsNumber);
        int expectedResult = 1000;

        for (int i = 0; i < 1000; i++) {
            int value = (int) (Math.random() * 100); // Generate a random integer
            stack.push(value); // we fill up the stack
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);

        for (int i = 0; i < threadsNumber; i++) {
            executor.submit(() -> { // add the task(s) to our pool
                try {
                    barrier.await(); //wait for all threads
                } catch (Exception e) {
                    e.printStackTrace();
                }

                while (stack.pop() != null) {
                    popOperations.addAndGet(1); //we pop values until the stack is empty
                }
            });
        }

        executor.shutdown(); // execute all tasks and terminate the pool 
        while (!executor.isTerminated()) {
            // Wait for all threads to finish.
        }

        // Ensure that the popOperations of popped elements equals the original popOperations
        assertEquals(expectedResult, popOperations.get());
    }
}

