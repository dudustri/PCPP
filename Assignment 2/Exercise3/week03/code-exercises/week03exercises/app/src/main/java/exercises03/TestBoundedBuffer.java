package exercises03;


public class TestBoundedBuffer {

    public void testConcurrency() throws InterruptedException {
        final int bufferSize = 10;

        // Create a bounded buffer with a semaphore
        BoundedBufferSemaphore<Integer> buffer = new BoundedBufferSemaphore<>(bufferSize);

        // Create a consumer thread
        Thread consumerThread = new Thread(() -> {
            try {
                for (int i = 0; i < bufferSize * 2; i++) {
                    Integer item = buffer.take();
                    Thread.sleep(150); // Simulate some work
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        consumerThread.start();

        Thread producerThread = new Thread(() -> {
            try {
                for (int i = 0; i < bufferSize * 2; i++) {
                    buffer.insert(i);
                    Thread.sleep(100); // simulate work
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // start the producer thread
        producerThread.start();

        // wait for both threads to finish
        producerThread.join();
        consumerThread.join();
    }

    public static void main(String[] args) {
        TestBoundedBuffer test = new TestBoundedBuffer();
        try {
            test.testConcurrency();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
