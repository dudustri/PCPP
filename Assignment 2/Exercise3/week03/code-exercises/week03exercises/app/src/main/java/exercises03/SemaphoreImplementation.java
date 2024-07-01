package exercises03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreImplementation {
    
    private final ReentrantLock l = new ReentrantLock(true); // fair flag: the thread waiting the longest in the queue is selected next
    private final Condition condition = l.newCondition(); //condition queue

    private volatile int permits; //a permit can be changed
    private final int cap; //the cap is always the same
    
    //semaphore constructor with number of permits as parameter
    public SemaphoreImplementation(int numOfPermits){
        this.permits = numOfPermits; //permits can be changed along the way
        this.cap = numOfPermits; //cap is final, always the same

    }

    public void acquire(){
        l.lock(); //only one thread can acquire at a time
        try {
            while(permits == 0){ //if there are no permits, wait in the queue until one is released
                condition.await(); //wait in queue...-> (see release)
            }
            permits--; //one is availabe, take it
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }

    public void release(){
        l.lock(); //only one thread can release at a time
        try {
            permits++; //release a permit
            condition.signal(); //release only one thread waiting in the queue 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }

    public int getPermits(){
        return this.permits;
    }

    public int getCap(){
        return this.cap;
    }
}