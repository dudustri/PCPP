package week1exercises;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Printer {

    PrinterMachine p1 = new PrinterMachine();
    Lock l = new ReentrantLock();

    public Printer() {

        Thread t1 = new Thread(() -> {
            while (true) {
                //l.lock();
                p1.print();
                //l.unlock();
            }
        });
 

        Thread t2 = new Thread(() -> {
            while (true) {
                //l.lock();
                p1.print();
                //l.unlock();
            }
        });

        t1.start();
        t2.start();
    }

    public static void main(String[] args) {
        new Printer();
    }
    class PrinterMachine {

        public void print() {
            System.out.print("-");
            try { Thread.sleep(50); } catch (InterruptedException exn) { }
            System.out.print("|");
        }
    }
}
