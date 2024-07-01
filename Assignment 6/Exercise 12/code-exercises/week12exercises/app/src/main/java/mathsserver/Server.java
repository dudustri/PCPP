package mathsserver;

// Hint: The imports below may give you hints for solving the exercise.
//       But feel free to change them.

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.ChildFailed;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.*;

import java.util.Queue;
import java.util.List;
import java.io.BufferedInputStream;
import java.util.LinkedList;
import java.util.stream.IntStream;

import mathsserver.Task;
import mathsserver.Task.BinaryOperation;
import mathsserver.Worker.ComputeTask;
import mathsserver.Worker.WorkerCommand;

public class Server extends AbstractBehavior<Server.ServerCommand> {
	/* --- Messages ------------------------------------- */
	public interface ServerCommand {
	}

	public static final class ComputeTasks implements ServerCommand {
		public final List<Task> tasks;
		public final ActorRef<Client.ClientCommand> client;

		public ComputeTasks(List<Task> tasks,
				ActorRef<Client.ClientCommand> client) {
			this.tasks = tasks;
			this.client = client;
		}
	}

	public static final class WorkDone implements ServerCommand {
		ActorRef<Worker.WorkerCommand> worker;

		public WorkDone(ActorRef<Worker.WorkerCommand> worker) {
			this.worker = worker;
		}
	}

	/* --- State ---------------------------------------- */
	// To be implemented
	private final int maxWorkers, minWorkers;
	private final Queue<ActorRef<Worker.WorkerCommand>> idleWorkers;
	private final Queue<ActorRef<Worker.WorkerCommand>> busyWorkers;
	private final Queue<ComputeTask> pendingTasks;
	private final ActorRef<Server.ServerCommand> serverReference;
	private int workDoneCount;

	/* --- Constructor ---------------------------------- */
	private Server(ActorContext<ServerCommand> context,
			int minWorkers, int maxWorkers) {
		super(context);
		this.minWorkers = minWorkers;
		this.maxWorkers = maxWorkers;
		this.serverReference = this.getContext().getSelf();

		this.pendingTasks = new LinkedList<>();
		this.busyWorkers = new LinkedList<>();
		this.idleWorkers = new LinkedList<>();
		for (int i = 0; i < minWorkers; i++) {

			ActorRef<Worker.WorkerCommand> worker = getContext().spawn(Worker.create(this.serverReference),
					"worker" + i);
			getContext().watch(worker);
			idleWorkers.add(worker);
		}
	}

	/* --- Actor initial state -------------------------- */
	public static Behavior<ServerCommand> create(int minWorkers, int maxWorkers) {
		return Behaviors.setup(context -> new Server(context, minWorkers, maxWorkers));
	}

	/* --- Message handling ----------------------------- */
	@Override
	public Receive<ServerCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(ComputeTasks.class, this::onComputeTasks)
				.onMessage(WorkDone.class, this::onWorkDone)
				.onSignal(ChildFailed.class, this::onChildFailed)
				.onSignal(Terminated.class, this::onWorkerTerminated)
				.build();
	}

	/* --- Handlers ------------------------------------- */
	public Behavior<ServerCommand> onComputeTasks(ComputeTasks msg) {

		msg.tasks.forEach(task -> {

			if (!this.idleWorkers.isEmpty()) {
				ActorRef<Worker.WorkerCommand> worker = this.idleWorkers.remove();
				worker.tell(new Worker.ComputeTask(task, msg.client));
				this.busyWorkers.add(worker);
			} else if (this.busyWorkers.size() < this.maxWorkers) {
				ActorRef<Worker.WorkerCommand> worker = (getContext().spawn(Worker.create(this.serverReference),
						"worker" + this.busyWorkers.size()));
				// watch the worker status and keep tracking the signal
				getContext().watch(worker);
				worker.tell(new Worker.ComputeTask(task, msg.client));
				this.busyWorkers.add(worker);
			} else {
				this.pendingTasks.add(new ComputeTask(task, msg.client));
			}

		});

		return this;
	}

	public Behavior<ServerCommand> onChildFailed(ChildFailed failMsg) {

		ActorRef<Void> crashedChild = failMsg.getRef();

		System.out.print("----------------------------------------------------------------" + "\n");
		System.out.print("before remove list busyWorkers size -> " + this.busyWorkers.size() + "\n");
		// Remove the terminated worker from the idle or busy workers list
		this.idleWorkers.remove(crashedChild);
		this.busyWorkers.remove(crashedChild);

		System.out.print("after remove list busyWorkers size - idleWorkers size -> " + this.busyWorkers.size() + " - "
				+ this.idleWorkers.size() + "\n");
		// Spawn a new worker to replace the terminated one
		ActorRef<Worker.WorkerCommand> newWorker = getContext().spawn(Worker.create(this.serverReference),
				crashedChild.path().name());
		getContext().watch(newWorker);

		if (!pendingTasks.isEmpty()) {
			// HERE IS THE EXTRA WAY TO GET PENDING TASKS! ANSWER FOR QUESTION 12.1.6
			ComputeTask pendingTask = this.pendingTasks.remove();
			newWorker.tell(new Worker.ComputeTask(pendingTask.task, pendingTask.client));
			this.busyWorkers.add(newWorker);
			System.out.print("after add new worker to the busy busyWorkers size " + this.busyWorkers.size() + "\n");
		} else if (this.idleWorkers.size() < this.minWorkers) {
			this.idleWorkers.add(newWorker);
			System.out.print("after add new worker to the idle idleWorkers size " + this.idleWorkers.size() + "\n");
		} else {
			System.out.print(
					"New worker not created due to the elastic system (demand balanced) specifications - no pending task found and idle queue bigger than min size defined"
							+ "\n");
		}

		return this;
	}

	public Behavior<ServerCommand> onWorkerTerminated(Terminated terminated) {
		return this;
	}

	public Behavior<ServerCommand> onWorkDone(WorkDone msg) {

		if (!pendingTasks.isEmpty()) {
			System.out.print("pending task found sending it the the done worker: " + msg.worker + "\n");
			ComputeTask pendingTask = this.pendingTasks.remove();
			msg.worker.tell(new Worker.ComputeTask(pendingTask.task, pendingTask.client));
		} else {
			if (idleWorkers.size() < this.minWorkers) {
				System.out.print("pending task not found changing worker to idle status: " + msg.worker + "\n");
				this.busyWorkers.remove(msg.worker);
				this.idleWorkers.add(msg.worker);
			} else {
				msg.worker.tell(new Worker.Stop());
			}
		}
		this.workDoneCount++;
		System.out.print("\n" + "Work done counter: " + this.workDoneCount + "\n");
		return this;
	}
}
