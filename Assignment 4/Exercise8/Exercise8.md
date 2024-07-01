# Exercise 8

---
## Ex 8.1

### 8.1.1

A: ---------------|q.enq(x)|--|q.enq(y)|->
B: ---|q.deq(x)|------------------------->

Note: for a concurrent execution to be sequential consistent there must exist at least one
reordering of ops that produces a sequential execution where: 

1. ops happen one at a time
2. program order is preserved
3. execution satifies the specification of the object (FIFO queue in this case)

Yes, the execution above is sequentially consistent since we can do the reordering:
< q.enq(x), q.enq(y), q.deq(x) >

[Q]: Would this also be valid?:

    < q.enq(x), q.deq(x), q.enq(y) >

(specification is maintained but is order maintained?)

### 8.1.2

A: ---------------|q.enq(x)|--|q.enq(y)|->
B: ---|q.deq(x)|------------------------->

In order for the above to be linearizable, q.deq(x) and q.enq(x) would have to overlap
in some way. Since they do not, the execution is NOT linearizable.

### 8.1.3

A: ---| q.enq(x) |-->
B: ------|q.deq(x)|--------------->

The above execution is linearizable since we can do the following linearization:
< q.enq(x), q.deq(x) >

### 8.1.4

A: ---|q.enq(x)|-----|q.enq(y)|-->
B: --|       q.deq(y)          |->

The above execution is not linearizable since it is not sequential consistent.
Hence, there is no way to make sure that q.enq(y) happens before q.deq(y)

### 8.1.5

Analyzing the different executions we realized that the order in which p.deq(y) and q.deq(x)
are executed really does not make a difference in this case since it is two different objects. Thus, these are the different kinds of execution order where program order is preserved:

1: < p.enq(x), q.enq(x), q.enq(y), p.enq(y), q.deq(x), p.deq(y) >
P: xy
Q: xy
Does not hold since queue specification is violated

2: < q.enq(y), p.enq(y), p.enq(x), q.enq(x), q.deq(x), p.deq(y) >
P: yx
Q: yx
Does not hold since queue specification is violated

3: < p.enq(x), q.enq(y), p.enq(y), q.enq(y), q.deq(x), p.deq(y) >
P: xy
Q: yx
Does not hold since queue specification is violated

4: < q.enq(y), p.enq(x), q.enq(x), p.enq(y), q.deq(x), p.deq(y) >
P: xy
Q: yx
Does not hold since queue specification is violated

5: < q.enq(y), p.enq(x), p.enq(y), q.enq(x), q.deq(x), p.deq(y) >
P: xy
Q: yx
Does not hold since queue specification is violated

6: < p.enq(x), q.enq(y), q.enq(x), p.enq(y), q.deq(x), p.deq(y) >
P: xy
Q: yx
Does not hold since queue specification is violated

Naturally there exist other execution orders, however, as shown above none of them will hold 
the principle of not violating the queue specification.
As shown above none of them will hold the principle of not violating the queue specification.
So, the execution is NOT sequentially consistent.

---
## Ex 8.2

### 8.2.1

Identifying linearization points:
The linearization points where identified in the given code (lines: PU06 for the push operation and lines PO05 and PO07 for the pop operation). Push operation has only one linearization point due the while loop be controled by the compareAndSet and no size limit was declared. Therefore, in this case, it is when the new node is successfully linked as the top of the stack if the operation is successful. Pop operation has two linearization points: one when the operation is successful in compareAndSet line (as in the push operation), and one more when the stack is empty inside the do while (PO05).

Explanation of the correct implementation:
Looking to the linearization points it is possible to state that for both pop and push operations are guarantees:

- **atomicity** -> through the atomic operations ensuring that all the threads or are full executed or restarted with the new set values. i.e. atomic reference to keep tracking the top of the stack (oldHead) and then using it in the compareAndSet. It ensures that no other thread can change it while it's being read
- **consistency** -> as mentioned before ensuring the full or non execution at all avoids inconsistent states of the object. Also, it preserves a order LIFO complying with a stack definition.
- **isolation** -> due to the atomic reference it is possible to say that the operations are also isolated based on the linearization points since it guarantees a non interference between threads (i.e. race condition)
- **linearization** -> The linearization points identified ensures that the operations are linearized at some expected moments where it can be seen as an "instantant operation" helping to observe and define the correctness criteria in a concurrent environment.

### 8.2.2

See TestLockFreeStack. run -> gradle cleanTest test --tests TestLockFreeStack

### 8.2.3

See TestLockFreeStack. run -> gradle cleanTest test --tests TestLockFreeStack

---
## 8.3

### 8.3.1

1. `writerTryLock()`
- **Wait Free** - No thread is guaranteed to acquire the lock in a finite number of steps. Multiple threads
can try to acquire the lock at the same time and one might fail due to others acquiring it first.
- **Lock Free** - The method is lock free because it is guaranteed that at least one thread will be able 
to acquire the lock. If the lock is not acquired, the thread will not be blocked and will try again.
There is no guarantee that the thread will acquire the lock in a finite number of steps.
- **Obstruction Free** -  Yes. In the absence of contention, a single thread will acquire the lock successfully.

2. `writerUnlock()`
- **Wait Free** - No. This method does not apply to the wait-free condition in the typical sense, 
as it's only meant to be called by the thread that currently holds the lock.
- **Lock Free** - Yes. The method is lock free because it is guaranteed that at least one thread will be able
to release the lock which will garantee some progress in a finite number of steps.
- **Obstruction Free** - Yes. Obstruction by definition issn't a issue here because the method is 
typically called by the lock holder only. The method finishes in a finite number of steps.

3. `readerTryLock()`
- **Wait Free** - No. This method does not apply to the wait-free condition in the typical sense, 
as a particular thread trying to acquire a read lock is not guaranteed to complete in a finite number of steps because
of other reader threads and updates to the reader list
- **Lock Free** - Potentially Yes. A Thread can join the reader list despite possible contention from other threads.
- **Obstruction Free** - Yes. A single thread can modify the reader list successfully without interference.

4. `readerUnlock()`
- **Wait Free** - No. Removing a thread from the reader list may face delays due to contention and 
is thereby not guaranteed to complete in a finite number of steps.
- **Lock Free** - Yes. The method relies on compareAndSet to update the reader list, 
ensuring that some progress (A Thread can leave the reader list) is made in the face of contention.
- **Obstruction Free** - Yes. A single thread can try to remove itself from the reader list will succeed
in a finite number of steps if no other thread is trying to modify the reader list.

FEEDBACK: If we have a implementation where there is no loop, that is wait-free.
    If you try to update the readerList with while(CAS) it is lock-free

wait-free -> lock-free -> obstruction-free
    
There is stuff to fix, we can fix it and send to Raul if we want.


---
## 8.4

We could notice that the problem is in the linearization point E4 inside the enqueue method. It does not guarantee that the value x has been successfully placed in the queue because (items[slot] = x;) is declared after the while loop. Therefore, a possible interleaving (i.e.) is that another thread could try do dequeue right after E4 causing an inconsistent state of the queue, dequeuing a wrong value or even throwing an exception that the queue is empty. This can happen because item[slot] was not updated violating the FIFO requirement about the elements order (enqueue/dequeue).
