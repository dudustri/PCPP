# Exercises Week 12

## 12.1

### 12.1.1
For the busyworkers we use a queue which will hold the workers that
are currently busy processing a task. If a worker is not in the 'busyWorkers' queue
then it is in the 'idleWorkers queue'. Thus, we can keep track of the amount of idle/busy workers.
For the pending tasks we also use a queue which holds ComputeTasks instructions consisting of a 
reference to the given client and the tasks to be computed.

### 12.1.2
Firstly, the constructor gets the context from the akka-superclass. Next, we initialize minWorkers and maxWorkers
to the argument given and make a reference to this server itself. This is purely for convenience so it is easier to call.
Next, we initialize the pendingTasks, busyWorkers and idleWorkers queues as linked-lists and 
populate the the idleWorkers with the minimum amount of Workers. The reason why we initialize the pendingtasks 
as linked-lists is to simply have to tasks be processed in order and fairly manner.

### 12.1.3
For the first case (a) we check whether the idleWorkers queue is empty. If not, we remove one idle worker,
call worker.tell with the task and the given client. Lastly, we add the worker to the busy worker queue

For the second case (b) we check whether the size of the busyWorkers queue is smaller 
the amount maxWorkers we set initially. If it is, then we spawn a new worker, call .watch() on the worker 
to keep notified about the status of the worker, call .tell() with the new worker and the given client and 
lastly we add the worker to the busyWorkers queue.

For the last cast (c) we simply add the task to the pendingTasks queue with task and client as arguments.

### 12.1.4
To watch all workers we spawn, we call .watch() right after every time we spawn a new worker.
This includes the constructor, onComputeTasks() and in onChildFailed().
onChildFailed() is a function we create to handle the error that occurs when a workers fails.
In short, it removes the worker from idleWorkers and busyWorkers, creates a new worker,
calls .watch() on it, if there are pending tasks to be executed we send the worker straight to work again
and if there are less idle workers than the minimum amount, we add the worker to the idleWorkers queue.

Lastly, we add '.onSignal(ChildFailed.class, this::onChildFailed)' so that a message is sent when a worker fails.

### 12.1.5
Within the onWorkDone method we check whether there are any pending tasks to be executed in the pendingTasks queue.
If there is, we remove the pending task from the queue and give it to a worker.
If not, we remove the worker from the busyWorkers queue and add it to the idleWorkers queue. 
Lastly, within createReceive we add '.onMessage(WorkDone.class, this::onWorkDone)' to listen for the WorkDone-message.

