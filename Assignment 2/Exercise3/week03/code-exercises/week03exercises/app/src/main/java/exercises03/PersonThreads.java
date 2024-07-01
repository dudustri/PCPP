package exercises03;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

public class PersonThreads {
    //we create a constructor for PersonThreads
    public PersonThreads() {
        int N = 100; //the amount of iterations every thread does
        final int threadsNumber = 10; //amount threads
        CyclicBarrier barrier = new CyclicBarrier(threadsNumber + 1);
        //barrier to make threads wait until critical section

        for (int i = 0; i < threadsNumber; i++) {
            final int threadIndex = i;
            Thread t = new Thread(() -> {
                try {
                    barrier.await(); //wait for all other threads
                    for (int j = 0; j < N; j++) { //loop N times
                        Person pt = new Person(); //we create a person object
                        pt.setAdressZip(2300 + threadIndex, "Rued Langgaards Vej"); //set the address and zip
                        pt.setName("User" + threadIndex); //set the name to the threadIndex
                        System.out.println("thread " + threadIndex + " terminated ->" +
                                pt.getId() + pt.getName() + " " + pt.getZip() + " " + pt.getAdress());
                        System.out.println(pt.getLastid());
                    }
                    barrier.await(); //wait for all others
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start(); //start the thread
        }

        try {
            barrier.await();
            barrier.await();
        } catch (Exception e) {
            System.out.println("Some thread was interrupted" + e);
        }
    }

    public static void main(String[] args) {
        new PersonThreads();
    }
}
