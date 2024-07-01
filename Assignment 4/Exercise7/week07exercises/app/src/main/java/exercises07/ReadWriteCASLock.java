 // For week 7
// raup@itu.dk * 10/10/2021

package exercises07;

// Very likely you will need some imports here
import java.util.concurrent.atomic.AtomicReference;

class ReadWriteCASLock implements SimpleRWTryLockInterface {

    private final AtomicReference<Holders> holders = new AtomicReference<>(); // holds references to the current holders of the lock

    public boolean readerTryLock() {
        // try to acquire the lock
        while (true) {  

            Holders current = holders.get(); // gets the current holder of the lock (if any) or null

            if (current instanceof Writer) {
                // The lock is held by a writer, so a reader cannot acquire the lock.
                return false;
            }

            // Create a new ReaderList with the current thread at the head,
            // and the existing list (if any) as the next element.
            ReaderList newReaderList = new ReaderList(Thread.currentThread(), (ReaderList) current);

            // Try to atomically update the holders from the current state to the new state.
            // basically, we try to add a new reader to ReaderList
            if (holders.compareAndSet(current, newReaderList)) {
                return true; // New reader successfully acquired a lock
            }
            // If the compareAndSet failed, another thread must have changed the holders
            // (either acquiring or releasing a lock), so the loop will retry.

            //[Q]: is this loop a case of busy-wait? We are in doubt...
        }
    }
    
    public void readerUnlock() {
        while (true) {
            Holders currentHolders = holders.get();

            // Check if holders is:
            //i)  not null = there is currently someone holding a lock
            //ii) refers to a ReaderList = it is that are holding the lock
            if (currentHolders == null || !(currentHolders instanceof ReaderList)) {
                throw new IllegalStateException("Lock not held by a reader");
            }

            ReaderList readerList = (ReaderList) currentHolders; // cast currentHolders to ReaderList
            Thread currentThread = Thread.currentThread(); // get the current thread

            // iii) Check if the current thread is in the reader list
            if (!readerList.contains(currentThread)) {
                throw new IllegalStateException("Current thread does not hold a read lock");
            }

            // Create a new reader list without the current thread
            ReaderList updatedList = readerList.remove(currentThread); // remove the current thread from the reader list

            // Atomically update holders if it hasn't changed meanwhile
            if (holders.compareAndSet(currentHolders, updatedList)) { // update holders to updatedList
                return; // Successfully updated, exit method
            }

            // If compareAndSet failed, retry the loop since holders might have been updated by another thread
        }
    }

    @Override
    public boolean writerTryLock() {
        if (holders.get() == null) { // No one holds the lock
            // The lock is unheld. Try to atomically set the holders to a new Writer object.
            return holders.compareAndSet(null, new Writer(Thread.currentThread())); // if match null, set to new Writer object
        }
        return false; // Lock is already held.
    }

    public void writerUnlock() { 

		Holders currentHolder = holders.get(); // gets the current holder of the lock

        // Check if the current holder is a Writer and the writer is the current thread.
        if (currentHolder instanceof Writer && ((Writer)currentHolder).thread == Thread.currentThread()) {
            // Attempt to set holders to null, releasing the lock.
            // if the holder is the calling thread -> lock releases
            if (!holders.compareAndSet(currentHolder, null)) { // if correct currentHolder, set to null
                // state changed in between the operations. This should never happen.
                throw new IllegalStateException("Lock state changed unexpectedly");
            }
        } else {
            // If we're not holding the lock, throw an exception.
            throw new IllegalStateException("Cannot unlock write lock when it's not held by the current thread");
        }
    }


    // Challenging 7.2.7: You may add new methods
    private static abstract class Holders { }

    private static class ReaderList extends Holders {
        private final Thread thread; // thread that holds the read lock
        private final ReaderList next; // another reader that also holds the read lock

        public ReaderList(Thread thread, ReaderList next) {
            this.thread = thread; // the thread that holds the lock
            this.next = next; // next node of the Thread that holds the read lock
        }

        public boolean contains(Thread thread) {
            // checks if the thread is in the list
            for (ReaderList current = this; current != null; current = current.next) {
                if (current.thread.equals(thread)) {
                    return true;
                }
            }
            return false;
        }

        public ReaderList remove(Thread thread) {
            if (this.thread.equals(thread)) {
                return this.next;
            } else if (this.next == null) {
                return this;
            } else {
                return new ReaderList(this.thread, this.next.remove(thread));
            }
        }
    }

    private static class Writer extends Holders {
        public final Thread thread;

        public Writer(Thread thread) {
            this.thread = thread; // the thread that holds the lock
        }
    }

    public static void main(String[] args) {
        // test wirteTryLock
        ReadWriteCASLock lock = new ReadWriteCASLock();
        // create two threads
        Thread t1 = new Thread(() -> {
            System.out.println("t1: " + lock.writerTryLock());
        });
        Thread t2 = new Thread(() -> {
            System.out.println("t2: " + lock.writerTryLock());
        });
        // try to unlock without holding the lock
        Thread t3 = new Thread(() -> {
            try {
                lock.writerUnlock();
            } catch (IllegalStateException e) {
                System.out.println("t3: " + e.getMessage());
            }
        });
    }
}
