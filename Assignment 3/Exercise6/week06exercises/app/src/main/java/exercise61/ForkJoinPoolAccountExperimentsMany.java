package exercise61;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ForkJoinPoolAccountExperimentsMany {

    static final int N = 10; //Number of threads
    static final int NO_TRANSACTION=5; //Number of transactions
    static final int NO_THREADS = 10; //Number of threads 
    static final Account[] accounts = new Account[N]; //Number of accounts
    
    static final ForkJoinPool pool = new ForkJoinPool(NO_THREADS); // pool can run NO_THREADS in parallel
    
    static Random rnd = new Random();
    
    public static void main(String[] args){ new ForkJoinPoolAccountExperimentsMany(); }

    public ForkJoinPoolAccountExperimentsMany(){
        //create empty accounts
        for (int i = 0; i < N; i++) {
            accounts[i] = new Account(i); //we create all the accounts, 'i' is simply the id
        }
        for( int i = 0; i<NO_THREADS; i++){
            // each transaction can be potentially executed in parallel by different threads in the pool (forkjoin).
            pool.submit(() -> doNTransactions(NO_TRANSACTION)); //submit a task to the pool
        }
        pool.shutdown(); // won't accept new tasks, but will finish the ones already submitted
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // wait for all tasks to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doNTransactions(int noTransactions){
        for(int i = 0; i<noTransactions; i++){
            long amount = rnd.nextInt(5000)+100;
            int source = rnd.nextInt(N);
            int target = (source + rnd.nextInt(N-2)+1) % N; // make sure target <> source
            doTransaction( new Transaction( amount, accounts[source], accounts[target]));
        }
    }

    private static void doTransaction(Transaction t){
        System.out.println(Thread.currentThread().getName() + " - " + t); // name of the current Thread + along with the transaction
        t.transfer(); //perform transfer operation
    }

    static class Transaction {
        final Account source, target;
        final long amount;
        Transaction(long amount, Account source, Account target){
            this.amount = amount;
            this.source = source;
            this.target = target;
        }

        public void transfer(){
            Account min = accounts[Math.min(source.id, target.id)]; // avoid deadlock pic min source
            Account max = accounts[Math.max(source.id, target.id)]; // avoid deadlock pic max source
            synchronized(min){ // lock the smallest account first
                synchronized(max){ // lock the largest account second
                    source.withdraw(amount);
                    try{Thread.sleep(50);} catch(Exception e){}; // Simulate transaction time
                    target.deposit(amount);
                }
            }
        }

        public String toString(){
            return "Transfer " + amount + " from " + source.id + " to " + target.id; 
        }
    }

    static class Account{
        // should have transaction history, owners, account-type, and 100 other real things
        public final int id;
        private long balance = 0;
        Account( int id ){ this.id = id;}
        public void deposit(long sum){ balance += sum; }
        public void withdraw(long sum){ balance -= sum; }
        public long getBalance(){ return balance; }
    }

}

