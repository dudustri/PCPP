package exercise63;

public class Histogram2 implements Histogram {

    private final int[] counts;

    /***
     * constructor for Histogram2
     * @param span
     */
    public Histogram2(int span) {
        this.counts = new int[span];
    }

    // Synchronized method to ensure atomicity
    public synchronized void increment(int bin) {
        counts[bin] = counts[bin] + 1;
    }

    // Synchronized method to ensure atomicity
    public synchronized int getCount(int bin) {
        return counts[bin];
    }

    // No need for synchronization since it just returns the length, which is immutable
    public int getSpan() {
        return counts.length;
    }
}
