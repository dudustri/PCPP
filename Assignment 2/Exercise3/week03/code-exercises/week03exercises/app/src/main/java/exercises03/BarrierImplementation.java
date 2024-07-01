package exercises03;

public class BarrierImplementation {
    private final int parties; //amount of threads to wait for
    private volatile int count = 0;
    private final SemaphoreImplementation semaphore;

    //BarrierImplementation constructor
    public BarrierImplementation(int numOfparties) {
        parties = numOfparties; 
        //we set parties to the given parameter 'numOfparties'
        semaphore = new SemaphoreImplementation(0);
        //we set 

    }

    public void await() {
        try {
            synchronized (this) {
                count++; //one more thread waiting

                if (count == parties) { //if all threads have arrived release all of them
                    for (int i = 0; i < parties; i++) {
                        semaphore.release(); // Release the semaphore for other waiting threads
                    }
                }
                else {
                    System.out.println("waits...");
                }
            }
            semaphore.acquire(); // Wait until all threads have arrived
            synchronized (this) {
                count--; //reset the counter
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BarrierImplementation barrier = new BarrierImplementation(3); // Create a barrier for two parties

        Thread t1 = new Thread(() -> {
            barrier.await();
            System.out.println("Thread 1 done");
        });

        Thread t2 = new Thread(() -> {
            barrier.await();
            System.out.println("Thread 2 done");
        });

        Thread t3 = new Thread(() -> {
            barrier.await();
            System.out.println("Thread 3 done");
        });

        Thread t4 = new Thread(() -> {
            barrier.await();
            System.out.println("Thread 4 done");
        });

        t1.start();t2.start();t3.start();t4.start();

        try {
            t1.join();t2.join();t3.join();t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



