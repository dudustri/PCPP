// REMINDER: You should only modify the Server.java file
package mathsserver;

// Helper data structure to encapsule tasks

public class Task {

    public enum BinaryOperation {SUM,SUB,MUL,DIV}
    
    public final int x,y;
    public final BinaryOperation op;

    public Task(int x, int y, BinaryOperation op) {
		this.x  = x;
		this.y  = y;
		this.op = op;
    }

    @Override
    public String toString() {
		return x + " " + op + " " + y;
    }
}
