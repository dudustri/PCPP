
package week1exercises;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterThreads2Covid {

    long counter = 0;
    final long PEOPLE  = 10_000;
    final long MAX_PEOPLE_COVID = 15_000;
    Lock l = new ReentrantLock();

    public CounterThreads2Covid() {
		try {
			Turnstile turnstile1 = new Turnstile();
			Turnstile turnstile2 = new Turnstile();

			turnstile1.start();turnstile2.start();
			turnstile1.join();turnstile2.join();

			System.out.println(counter+" people entered");
		}
		catch (InterruptedException e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
		new CounterThreads2Covid();
    }


    public class Turnstile extends Thread {
		public void run() {
			try {
				for (int i = 0; i < PEOPLE; i++) {
					l.lock();
					if (counter == MAX_PEOPLE_COVID) {break;}	
					counter++;
					l.unlock();
					
				}
			} finally {
				l.unlock();
				System.out.println("done");
			}
		}
    }
}
