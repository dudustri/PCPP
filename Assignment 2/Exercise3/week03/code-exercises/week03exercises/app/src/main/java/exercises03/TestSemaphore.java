package exercises03;

public class TestSemaphore {
    public static void main(String[] args) {
        SemaphoreImplementation semaphore = new SemaphoreImplementation(3); // Change the number of permits as needed

        // Create multiple threads that will try to acquire and release permits
        Thread[] threads = new Thread[500]; // You can adjust the number of threads as needed

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    // Attempt to acquire permits
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " acquired a permit.");
                    Thread.sleep(1000); // Simulate some work with the permit
                    System.out.println(Thread.currentThread().getName() + " releasing the permit.");
                    semaphore.release(); // Release the permit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have completed.");
    }
}