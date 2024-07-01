package exercises03;

import java.util.concurrent.locks.ReentrantLock;

public class TestPerson {
    public TestPerson(){
        int N = 200000;

        Person p = new Person();
        ReentrantLock l = new ReentrantLock();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < N ; i++) {
                try {
                    p.setAdressZip(2300, "Rued Langgaards Vej");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < N ; i++) {
                try {
                    p.setAdressZip(2200, "JagtVej");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start(); t2.start(); //
        try { t1.join(); t2.join(); }  
        catch (InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }
        System.out.println(p.getZip());
        System.out.println(p.getAdress());
    }
    public static void main(String[] args) {
        new TestPerson();
    }
}




