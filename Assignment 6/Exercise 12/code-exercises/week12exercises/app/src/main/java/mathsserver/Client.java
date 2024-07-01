// REMINDER: You should only modify the Server.java file
package mathsserver;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.Random;

import mathsserver.Task;
import mathsserver.Task.BinaryOperation;

public class Client extends AbstractBehavior<Client.ClientCommand> {
	/* --- Messages ------------------------------------- */
	public interface ClientCommand {
	}

	public static final class ClientStart implements ClientCommand {
	}

	public static final class TaskResult implements ClientCommand {
		public final Task task;
		public final int result;

		public TaskResult(Task task, int result) {
			this.task = task;
			this.result = result;
		}
	}

	/* --- State ---------------------------------------- */
	ActorRef<Server.ServerCommand> server;
	List<Task> tasks;

	/* --- Constructor ---------------------------------- */
	private Client(ActorContext<ClientCommand> context,
			ActorRef<Server.ServerCommand> server) {
		super(context);
		this.server = server;
		tasks = new ArrayList<Task>();
	}

	/* --- Actor initial state -------------------------- */
	public static Behavior<ClientCommand> create(ActorRef<Server.ServerCommand> server) {
		return Behaviors.setup(context -> new Client(context, server));
	}

	/* --- Message handling ----------------------------- */
	@Override
	public Receive<ClientCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(ClientStart.class, this::onClientStart)
				.onMessage(TaskResult.class, this::onTaskResult)
				.build();
	}

	/* --- Handlers ------------------------------------- */
	public Behavior<ClientCommand> onClientStart(ClientStart msg) {

		Random r = new Random();
		tasks.add(new Task(r.nextInt(), 0, BinaryOperation.DIV));
		tasks.add(new Task(r.nextInt(), 0, BinaryOperation.SUM));
		tasks.add(new Task(r.nextInt(), 0, BinaryOperation.SUB));
		tasks.add(new Task(r.nextInt(), 0, BinaryOperation.MUL));
		tasks.add(new Task(r.nextInt(), 1, BinaryOperation.DIV));
		tasks.add(new Task(r.nextInt(), 0, BinaryOperation.SUM));
		tasks.add(new Task(r.nextInt(), 0, BinaryOperation.SUB));
		tasks.add(new Task(r.nextInt(), 1, BinaryOperation.MUL));

		server.tell(new Server.ComputeTasks(tasks, getContext().getSelf()));

		getContext().getLog().info("{} started.",
				getContext().getSelf().path().name());
		getContext().getLog().info(tasks.toString());
		return this;
	}

	public Behavior<ClientCommand> onTaskResult(TaskResult msg) {
		getContext().getLog().info("{}: Result of task {} is {}",
				getContext().getSelf().path().name(),
				msg.task, msg.result);
		return this;
	}
}
