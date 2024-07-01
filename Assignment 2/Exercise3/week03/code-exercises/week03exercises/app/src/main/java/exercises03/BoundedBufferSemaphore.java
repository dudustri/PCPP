// For week 3
// raup@itu.dk * 18/09/2021

package exercises03;

import java.util.concurrent.Semaphore;

/*For the sake of academic integretity, we have drawn inspiration from the
implementation in Goetz page 249*/

public class BoundedBufferSemaphore<T> implements BoundedBufferInteface<T> {
    //we define two semaphores: one for consumers and one for producers
    private final Semaphore consumerSema, producerSema, mutex;

    //creating a collection of our items
    private final T[] items;

    //initial positions to insert and take from (volatile unessary)
    private volatile int insertPos = 0, takePos = 0;

    //boundedbuffer constructor
    public BoundedBufferSemaphore (int cap){ 

        // we create  a mutex to ensure mutual exclusion ! 
        // hence, only one producer/consumer can insert/take the buffer at a time
        mutex = new Semaphore(1);

        //we create a semaphore for the consumers with 0 permits and a fair flag
        //since it should await for the producers to produce something first
        consumerSema = new Semaphore(0, true);

        //create a semaphore for the producers with permits equal total capacity
        //since the collection is empty when we start
        producerSema = new Semaphore(cap, true);

        //we create a buffer with generic type and the size equal to capacity
        items = (T[]) new Object[cap];
    }
    
    /*The relation between consumer and producer semaphores 
    satisfy the bounded buffer specification 
    because it will block the consumer when the buffer is empty*/

    @Override
    public T take() throws Exception {
        //the consumer tries to acquire a permit, if none available it waits
        consumerSema.acquire();
        //acquire mutex resource - mutual exclusion
        mutex.acquire();
        //consumes it
        T item = this.consume();
        //release mutex resource
        mutex.release();
        //since one item has been consumed, we release one permit for the producers
        producerSema.release();
        //return item
        return item;
    }

    @Override
    public void insert(T elem) throws Exception {
        //the producer tries to acquire a permit, if none available it waits
        producerSema.acquire();
        //acquire mutex resource - mutual exclusion
        mutex.acquire();
        //produce an item
        T item = this.produce(elem);
        //release mutex resource
        mutex.release();
        //since one item has been produced, we release one permit for the consumers
        consumerSema.release();
    }

    private T consume() {
        //we get take position
        int i = takePos;
        //x is the item being consumed
        T x = items[i];
        //we clear the space
        items[i] = null;
        //update takePos position for the next element or reset it when reach the buffer maximum size
        takePos = (++i == items.length)? 0 : i;
        //return the item that has been consumed
        return x;
    }

    private T produce(T x) {
        //we get the insert position
        int i = insertPos;
        //we insert item into the buffer
        items[i] = x;
        //update takePos position for the next element or reset it when reach the buffer maximum size
        insertPos = (++i == items.length)? 0 : i;
        //return the item that has been produced
        return x;
    }
}

//[Question] - Does it make difference placing the mutex before or after the consumer/producer bouncer/semaphore?
