package exercises07;
import java.util.concurrent.atomic.AtomicInteger;

public class CasHistogram implements Histogram {
    private final AtomicInteger[] bins; 

    public CasHistogram(int span){
        this.bins = new AtomicInteger[span];  // we create a collection with size equal to 'span'
        
        for (int i = 0; i < span; i++) {
            bins[i] = new AtomicInteger(0);  // we populate the collection with AtomicIntegers
        }
    }
    
    public static void main(String[] args) {
        CasHistogram c1 = new CasHistogram(10);
        c1.getAndClear(5);
    }

    public void increment(int bin){
        AtomicInteger binNum = bins[bin];  // we get a reference to the bin we want to increment

        int oldValue, newValue;  // define two integers
        do {
            oldValue = binNum.get();  // the oldValue is the current value within the bin
            newValue = oldValue + 1;   // the newValue becomes oldValue + 1
        } while(!binNum.compareAndSet(oldValue, newValue)); 
        // compareAndSet:
        // if the value within the bin is equal to oldValue, binNum is set to newValue and returns true
        // otherwise no update is performed, returns false and we repeat the process
    }

    public int getCount(int bin){
        return bins[bin].get(); // return the number in the given bin
    }

    public int getSpan(){
        return bins.length; // return the size of the bins
    }

    public int getAndClear(int bin) {
        AtomicInteger binNum = bins[bin]; // we get a reference to value within the bin
        int currentVal; // the current value within the bin
        do {
            currentVal = binNum.get(); // the current value within the bin
        } while (!binNum.compareAndSet(currentVal, 0));
        //if the value within the bin is equal to currentVal, we set it to 0
        return currentVal;
        //we return the value within the bin before is was updated to 0
    }
    
}