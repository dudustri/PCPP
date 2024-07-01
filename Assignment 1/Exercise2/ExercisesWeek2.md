Exercise 2.1 Consider the Readers and Writers problem we saw in class. As a reminder, here is the specification of the problem:
• Several reader and writer threads want to access a shared resource.
• Many readers may access the resource at the same time as long as there are no writers.
• At most one writer may access the resource if and only if there are no readers.

1. Use Java Intrinsic Locks (i.e., synchronized) to implement a monitor ensuring that the access to the shared resource by reader and writer threads is according to the specification above. You may use the code in the lectures and Chapter 8 of Herlihy as inspiration, but do not feel obliged copy that structure.

2. Is your solution fair towards writer threads? In other words, does your solution ensure that if a writer thread wants to write, then it will eventually do so? If so, explain why. If not, modify part 1. so that your implementation satisfies this fairness requirement, and then explain why your new solution satisfies the requirement.

- Whether a writer will be allowed to write or not depends on two critical places in our code:

  (Line 43-44): if(addReader==){
  condition.signalAll();}
  (Line 62-63): while(readsAcquires != signOffReader){
  condition.await();}

  The four lines above ensures absence of starvation since
  the writer threads does NOT wait for ALL readers to unlock (as before).
  Instead, the writer threads simply waits for the current reader (or writer)
  to finish. Say for an example that a writer is 'stuck' in the condition queue
  waiting to be released to the lock queue. Before, the writer would have to wait until
  all readers where finished:

  OLD: if(readers==0)
  condition.signalAll();

  Whereas now all writers (or readers) will be released from the condition queue
  as soon the current reader is done (condition.signalAll());

CHALLENGE 3. Is it possible to ensure absence of starvation using ReentrantLock or intrinsic java locks (synchronized)?
Explain why

- To ensure absence of starvation using ReentrantLock or intrinsic locks we would have to make sure that all threads enter their critical section at some point during execution. One way to achieve that would be to use the Reentrant lock's fair flag (new Reentrant lock(true)). In short, this would mean that threads would be given access to their respective critical section based on the time they have been waiting.

Exercise 2.2 Consider the lecture’s example in file TestMutableInteger.java, which contains this definition of class MutableInteger.

1. Execute the example as is. Do you observe the "main" thread’s write to mi.value remains invisible to the t thread, so that it loops forever? Independently of your observation, is it possible that the program loops forever? Explain your answer.

   - Yes, it is possible to observe that when we write to mi.value it is not visible for thread t. It happens because of the lack of synchronized operations allowing the main thread to cache the initial value close to the CPU of thread t (not the main one). Thus, thread t does not access the proper modified value due to the absence of a happens before condition (syncronization). Therefore, yes, it is possible that thread t runs forever if the data modified by the main thread keeps being invisible to it.

2. Use Java Intrinsic Locks (synchronized) on the methods of the MutableInteger to ensure that thread t always terminates. Explain why your solution prevents thread t from running forever.

   - This solution prevents thread t from always terminating because after implementing the intrinsic locks we are synchronizing both threads thereby forcing them to share resources and make the data visible. In this case we synchronize the method used by the thread t to access the shared resources. Therefore, modifying the get method to "public synchronized int get()" makes the data visible since it does not allow to cache this value in the memory anymore. It reduces performance but solves the problem of the shared resources.
   (the unlock does the flushing)

3. Would thread t always terminate if get() is not defined as synchronized? Explain your answer.

   - Thread t would not terminate if get() was not defined as synchronized because it is the critical section of the thread (reading section to access the shared resource).

4. Remove all the locks in the program, and define value in MutableInteger as a volatile variable. Does thread t always terminate in this case? Explain your answer.

   - Yes, it terminates because when we declare a variable as volatile we are using a weak form of synchronization not allowing the data to be stored in the lower levels of the cache making it visible for all other threads (keep the information in the shared memory level). It does not ensure mutual exclusion but once we are reading from the shared resource (and thereby not caring about writing) it is plausible to do so.

5. Explain parts 3. and 4. in terms of the happens-before relation.

   - Both locking the threads (synchronization) and using volatile variables are a way to prevent the CPU or the JIT compiler (in this case that we are using java) to reorder operations and enforce visiblity once we have established the happens-before relation. Therefore, when we implement a happens-before relation we do not allow the CPU to keep the value close to it (low caching levels), forcing it to be flushed to the main accessible memory. Doing the process of reordering it is possible to improve performance but we lose the ability to ensure resource sharing in a good way (using the happens-before relation). The volatile variable does not guarantee mutual exclusion and therefore is not a good choice when we need to write something using multiple threads. However, in this case it is still an okay option because thread t is only reading the data being writted by the main thread. This works fine because writing to a volatile variable always comes before reading from it (happens-before relation). In other words, the write process happens before the reading. So: 
   t1 main (m.set(42)) -> t2 thread t (while(m.get())). 
   The same occurs when we synchronize the threads using the locks.

Exercise 2.3 Consider the small artificial program in file TestLocking0.java. In class Mystery, the single mutable field sum is private, and all methods are synchronized, so superficially the class seems that no concurrent sequence of method calls can lead to race conditions.

1. Execute the program several times. Show the results you get. Are there any race conditions?

   - Results: [1826812.000000, 1894721.000000, 1835455.000000, 1847959.000000, 1745641.000000]. 
   Yes, it is possible to notice that there is a race condition.

2. Explain why race conditions appear when t1 and t2 use the Mystery object. Hint: Consider (a) what it means for an instance method to be synchronized, and (b) what it means for a static method to be synchronized.

   - Because the threads are not rightly synchronized due to their reference. Thread t1 is synchronized with an instance of the class (this.) and the thread t2 is synchronized with the class itself (static). Once there is a lack of syncronization between the threads, it allows the race condition to happen because the thredas do not block each other (happens-before condition).

3. Implement a new version of the class Mystery so that the execution of t1 and t2 does not produce race conditions, without changing the modifiers of the field and methods in the Mystery class. That is, you should not make any static field into an instance field (or vice versa), and you should not make any static method into an instance method (or vice versa). Explain why your new implementation does not have race conditions.

   - The implementation works because now we are synchronizing both threads to write using a happens-before condition. To do that we simple add " synchronized(m) {thread t2 content} " thereby ensuring the synchronization of thread 2 (static by declaration) to the instance of m. Therefore, both threads will be synchronized and the result should be consistent without changes in the mystery class once it avoids race condition (locking process between threads and happens-before condition implementation).

4. Note that the method sum() also uses an intrinsic lock. Is the use of this intrinsic lock on sum() necessary for this program? In other words, would there be race conditions if you remove the modifier synchronized from sum() (assuming that you have fixed the race conditions in 3.)?

   - No, in this case it is not necessary because both threads are not using the sum method. Therefore there is no need of use the intrinsic lock in this method since it has no relevance and cannot trigger a race condition. If we remove the lock it will still work as it should returning the expected result.
