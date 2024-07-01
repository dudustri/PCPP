// For week 8
// raup@itu.dk * 2023-10-20
package exercises08;

import java.util.concurrent.atomic.AtomicReference;

// Treiber's LockFree Stack (Goetz 15.4 & Herlihy 11.2)
class LockFreeStack<T> {
	AtomicReference<Node<T>> top = new AtomicReference<Node<T>>(); // Initializes to null

	public void push(T value) {
		Node<T> newHead = new Node<T>(value); //PU01
		Node<T> oldHead; //PU02
		do { //PU03
			oldHead      = top.get(); //PU04
			newHead.next = oldHead; //PU05
		} while (!top.compareAndSet(oldHead,newHead)); //PU06 -> linearization point when element is successfully set
	    
	}
	    
	public T pop() {
		Node<T> newHead; //PO1
		Node<T> oldHead; //PO2
		do { //PO3
			oldHead = top.get(); //PO4
			if(oldHead == null) { return null; } //PO5 -> Linearization point when queue is empty
			newHead = oldHead.next; //PO6
		} while (!top.compareAndSet(oldHead,newHead)); //PO7 -> Linearization point when element is successfully removed
	    
		return oldHead.value; //PO8
	}   

	//FEEDBACK: Actually the linerization point is PO4 because that is where we the shit happens since you get the value. 
	// The line P05 is just a consequence based on the value we got in the linearization point.
    
    // class for nodes
    private static class Node<T> {
		public final T value;
		public Node<T> next;

		public Node(T value) {
			this.value = value;
			this.next  = null;	    
		}
    }
}
