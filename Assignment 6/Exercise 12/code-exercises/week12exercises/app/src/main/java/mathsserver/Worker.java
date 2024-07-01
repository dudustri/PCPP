// REMINDER: You should only modify the Server.java file
package mathsserver;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import mathsserver.Task;
import mathsserver.Task.BinaryOperation;

public class Worker extends AbstractBehavior<Worker.WorkerCommand> {
    /* --- Messages ------------------------------------- */
    public interface WorkerCommand { }

    
    public static final class ComputeTask implements WorkerCommand {
		public final Task task;
		public final ActorRef<Client.ClientCommand> client;

		public ComputeTask(Task task, ActorRef<Client.ClientCommand> client) {
			this.task = task;
			this.client = client;
		}
    }

    public static final class Stop implements WorkerCommand { }

    
    
    /* --- State ---------------------------------------- */
    ActorRef<Server.ServerCommand> server;

    /* --- Constructor ---------------------------------- */
    private Worker(ActorContext<WorkerCommand> context,
				   ActorRef<Server.ServerCommand> server) {
    	super(context);
		this.server = server;
    }


    /* --- Actor initial state -------------------------- */
    public static Behavior<WorkerCommand>
		create(ActorRef<Server.ServerCommand> server) {
    	return Behaviors.setup(context -> new Worker(context,server));
    }
    

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<WorkerCommand> createReceive() {
    	return newReceiveBuilder()
    	    .onMessage(ComputeTask.class, this::onComputeTask)
    	    .onMessage(Stop.class, this::onStop)
    	    .build();
    }


    /* --- Handlers ------------------------------------- */
    public Behavior<WorkerCommand> onComputeTask(ComputeTask msg) {
    	getContext().getLog().info("{}: computing task {}",
								   getContext().getSelf().path().name(),
								   msg.task);
		int result = taskExecutor(msg.task);
		msg.client.tell(new Client.TaskResult(msg.task,result));
		server.tell(new Server.WorkDone(getContext().getSelf()));	
    	return this;
    }

    public Behavior<WorkerCommand> onStop(Stop msg) {
		return Behaviors.stopped();
    }

    

    /* --- Auxiliary Functions--------------------------- */
    private int taskExecutor(Task t) {
		int returnValue = 0;
		if (t.op == BinaryOperation.SUM) { returnValue = Sum(t.x,t.y); }
		else if (t.op == BinaryOperation.SUB) { returnValue = Sub(t.x,t.y); }
		else if (t.op == BinaryOperation.MUL) { returnValue = Mul(t.x,t.y); }
		else if (t.op == BinaryOperation.DIV) { returnValue = Div(t.x,t.y); }
		return returnValue;
    }
    private int Sum(int x, int y) { return x+y; }
    private int Sub(int x, int y) { return x-y; }
    private int Mul(int x, int y) { return x*y; }
    private int Div(int x, int y) { return x/y; }
}
