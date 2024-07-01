package exercise63;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Histogram3 implements Histogram {
    private final int[] counts; //array for values
    private final Lock[] locks; //array for locks

    public Histogram3(int span, int nrLocks) {
        this.counts = new int[span];
        this.locks = new ReentrantLock[nrLocks]; // Create the locks
        for (int i = 0; i < nrLocks; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    private Lock getLockForBin(int bin) {
        // using a module operation to distribute the available locks over the bins
        return locks[bin % locks.length]; // Use modulo to avoid out of bounds
    }

    public void increment(int bin) {
        Lock lock = getLockForBin(bin); // Get the lock for the bin
        lock.lock(); // Lock the bin
        try {
            counts[bin] = counts[bin] + 1;
        } finally {
            lock.unlock();
        }
    }

    public int getCount(int bin) {
        Lock lock = getLockForBin(bin); // Get the lock for the bin
        lock.lock();
        try {
            return counts[bin]; // Return the count
        } finally {
            lock.unlock();
        }
    }

    public int getSpan() {
        return counts.length;
    }
}
