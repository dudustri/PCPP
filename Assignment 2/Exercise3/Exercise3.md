# Exercises week 3

## Exercise 3.1

### 3.1.1 - Implement a class BoundedBuffer<T> as described above using only Java Semaphore for synchronization— i.e., Java Lock or intrinsic locks (synchronized) cannot be used.

see 'BoundedBufferSemaphore.java'

### 3.1.2 - Explain why your implementation of BoundedBuffer<T> is thread-safe. Hint: Recall our definition of thread-safe class, and the elements to identify/consider in analyzing thread-safe classes (see slides).

To explain why our BoundedBufferSemaphore class is thread-safe, we may identify and consider the different elements which determines the thread-safety of a class:

• Class state
• Escaping
• (Safe) publication
• Immutability
• Mutual exclusion

#### Class state

- To ensure that a class state is thread-safe, there must be no data races.
  The variables of our class that may be shared between threads are: insertPos, takePos and items[].
  This is done by ensuring mutual exclusion with out mutex semaphore which basically makes sure that only one consumer or producer can access the shared ressources at a time.

#### Escaping

- As stated in the slides, we need to make sure that all state variables
  are defined as 'private' to make sure that no escaping will occur.
  Thus, the three variables (takePos, insertPos, item[]) are defined as private.

#### Safe publication

- To ensure safe publication, we must make that there is correct object initialization and visibility. To do that, we declare the two integers (takePos and insertPos) as volatile (since they are primitives). We declare the items array and semaphores as 'final' making all of them visible to all threads and making sure that all threads access the variables in their correct state.

#### Immutability -> Mutual Exclusion

- Our class is not immutable, so we need to ensure the mutual exclusion. To do so, we have implemented a mutex to make the implemented class a thread-safe one.

### 3.1.3 - Is it possible to implement BoundedBuffer using Barriers? Explain your answer

A barrier makes threads wait until all reached a specific point in execution.
Perhaps it could be done with two barriers; one for producers and one for consumers.
Producers would then wait for all other producers to arrive, then insert and lastly signal
to the consumers that items are ready to be taken.
Same for consumers, they would have to wait all for other consumers to arrive, take items and then signal to the producers that items can be inserted.
However, this approach is not efficient or even an appropriate implementation since the barriers are not designed to handle thread synchronization and managing the control of resources. It would require extra coding and a "go horse" approach to make it work. But, in our opinion: yes, it would be possible but definitely not better than use locks or semaphores.

FEEDBACK: NOT POSSIBLE :-( because of mutual exclusionm not possible to be implemented with barriers

### 3.1.4 (Challenge) - One of the two constructors to Semaphore has an extra parameter named fair. Explain what it does, and explain if it matters in this example. If it does not matter in this example, find an example where it does matter

The extra parameter 'fair' (boolean) guarantees that the threads are given permits in the order in which they tried to acquire them. In our case, we can think of it as two seperate lines/queues of producers and consumers respectively. The producer and consumer who has waited the longest will be given a permit to insert or take. In our implementation this will only make a difference in terms which order the different items are inserted and taken since the producers and consumers have each their own semaphore.

An example where it might matter would be a case with a shared database which only two users can read from at a time. If we set the Semaphore to a capacity of 2 and start five reader threads, the 'true' flag would ensure that the users read from the database in a fair and ordered manner (FIFO).

## 3.2

### 3.2.1 - Implement a thread-safe version of Person using Java intrinsic locks (synchronized). Hint: The Person class may include more attributes than those stated above; including static attributes.

See 'Person.java' for our implementation

### 3.2.2 - Explain why your implementation of the Person constructor is thread-safe, and why subsequent accesses to a created object will never refer to partially created objects.

To specify why our implementation of the Person class is thread-safe, we may
turn to the five topics of thread-safety:

#### Class state - No data races

To ensure no data races occur, we have made all methods synchronized thereby making
the threads mutually exclusive to one another when using the methods of the class.

#### Escaping -

All state variables are defined as 'private'. Thus, no escaping can occur.

#### Safe publication -

All primitive types are either declared as 'volatile', 'final' or initialised as '0' (lastId).
Thus, safe publication is ensured.

#### Immutability

The implemented Person class is not immutable. Therefore, it was ensured by mutual exclusion [see the next topic].

#### Mutual Exclusion

To ensure mutual exclusion, all methods have been declared with 'synchronized'
thereby providing intrinsic locks necessary for proper happens-before relations between
threads.

### ..."why subsequent accesses to a created object will never refer to partially created objects

Via 'final' we ensure that the id is set before the constructor completes.
this means that 'id' is always in a consistent state when accessed by other threads.

Via 'volatile' we ensure that all writes to these variables are visible to all other threads.

Lastly, since lastId is static (and volatile) we ensure that it is shared between all instances of person.

In short, the three points above ensure that the objects are always in a complete and not partial state.

### 3.2.3 - Implement a main thread that starting several threads the create and use instances of the Person class.

See 'TestPerson.java' and 'PersonThreads.java'.

### 3.2.4 Assuming that you did not find any errors when running 3. Is your experiment in 3 sufficient to prove that your implementation is thread-safe?

In our opinion, even that we didn't find any errors, it is not sufficient to prove that our implementation is thread-safe since there are several different possibilities for interleavings. However, it is a good indicator that the implementation is the right way. To make sure that it is a thread-safe class it is necessary test it repeatedly.

## 3.3 (Challenge)

### 3.3.1 - Implement a Semaphore thread-safe class using Java Lock. Use the description of semaphore provided in the slides.

See 'SemaphoreImplementation.java'

### 3.3.2 - Implement a (non-cyclic) Barrier thread-safe class using your implementation of Semaphore above. Use the description of Barrier provided in the slides.

See 'BarrierImplementation.java'
