package week1exercises;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLongCounterExperiments {
    
    LongCounter lc = new LongCounter();
    int counts = 20000000;
    Lock l = new ReentrantLock();

    public TestLongCounterExperiments() {

        Thread t1 = new Thread(() -> {
            for (int i=0; i<counts; i++){
                try{
                //l.lock();
                lc.increment();
                //l.unlock();
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i=0; i<counts; i++){
                //l.lock();
                lc.increment();
                //l.unlock();
            }
        });

        t1.start(); t2.start(); //
        try { t1.join(); t2.join(); }  // wait for both 100 100
        catch (InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }

        System.out.println("Count is " + lc.get() + " and should be " + 2*counts);
    }

    public static void main(String[] args) {
        new TestLongCounterExperiments();
    }

    class LongCounter {
        private volatile long count = 0;

        public void increment() {
            count++;
        }

        public synchronized void decrement() {
            count--;
        }

        public synchronized long get() {
            return count;
        }
    }
}

