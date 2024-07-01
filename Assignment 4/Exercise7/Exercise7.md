# Exercise 7


## Ex 7.1

### 7.1.1

1. - We have ensured that the class state do not escape by declaring 
the AtomicInteger[] bins as private. This ensures that the array will 
only be accessed through our defined methods which are public. Also,
this ensures that the bins are immutable. This means that the bins cannot be changed after they have been created.

2. - We have ensured safe publication by 1) using the AtomicInteger 
which operations are atomic. Thus, whenever a bin is updated it is visible to all other threads immediatly.
-2) We have initialized the bins-array as final which ensures that no other actions happens-before that.

The increment method ensures safe-publication for several different reason. 
Firstly, using 'get.()' is an atomic operation from 'AtomicInteger'. 
Secondly, the line 'binNum.compareAndSet(oldValue, newValue)'
is the most key one since it checks whether the value in the bin is 
still equal to oldvalue. If yes, the value within the bin is set to the new value(old value + 1), 
if not the loop simply repeats and tries again. 


### 7.1.2
See 'TestHistograms.java' in test-folder
Please note that in our implementation we're using `Histogram2` class instead of `Histogram1` class.
That's because in our implementation we used `Histogram2` for Assignment 7.3.3 to compute the prime 
factors. So to make sure our `CasHistogram` class is working we used `Histogram2` instead of `Histogram1`.

### 7.1.3
See `TestCASLockHistograms.java`

Result:
Range: 0..4.999.999 (5 mil)

| Implementation   | Time (ns)        | Standard Deviation | Thread Count |
|-------------------|-----------------|--------------------|--------------|
| `CASHistogram`    | 1492689366,5 ns | 59282854,90        | 8            |
| `Histogram2`      | 1605992231,8 ns | 94771565,16        | 8            |


The result above is indeed expected since CAS-operations are generally 
faster than acquiring locks. This is largely due to the fact that an 
unsuccessful CAS-operation does not cause thread-scheduling (does not the block the thread). 
As explained in 7.1.1, if the value within the bin has been changed when we run 'binNum.compareAndSet(oldValue, newValue)'  we simply repeat the process again. Also, CAS operations tend to scale better for high levels of concurrency (many threads). Even though we 'only' use 8 threads, it may play a part when testing which histogram is faster.


## 7.2

### 7.2.1
See `ReadWriteCASLock.java` see `writerTryLock()`

### 7.2.2
See `ReadWriteCASLock.java` see `writerUnlock()`

### 7.2.3
See `ReadWriteCASLock.java` see `readerTryLock()`

### 7.2.4
See `ReadWriteCASLock.java` see `readerUnlock()`

### 7.2.5
See `TestLocks.java` in specific 
the test methods:
- `testCannotTakeReadLockWhenHoldingWriteLock()`
- `testCannotTakeWriteLockWhenHoldingReadLock()`
- `testCannotUnlockReadLockNotHeld()`
- `testCannotUnlockWriteLockNotHeld()`

### 7.2.6
See `TestLocks.java` in specific 
the test methods:
- `twoWritersCannotAcquireLockSimultaneously()`

### Run performance tests:
gradle -PmainClass=exercises07.TestCASLockHistogram run

### Run correctness tests:
gradle cleanTest test --tests exercises07.TestHistograms run
gradle cleanTest test --tests exercises07.TestLocks run




