# 4.1

## 4.1.1 - Implement a functional correctness test that finds concurrency errors in the add(Integer element) method in ConcurrentIntegerSetBuggy. Describe the interleaving that your test finds.

Since we want to test the add() method of 'ConcurrentSetTest', we do N-many add() operations for every thread adding number from 0-49. This means that, if succesfull, the amount of intergers in the set should be equal to N. Since we have not ensured mutual exclusion of any kind, an possible interleaving could be:

t1(add) -> t2 (add) -> t3 (add) -> t1(update size) -> t2(update size) -> t3(update size)

In short, the issue is that subsequent add() operations of t2 and t3 due not receive the correct size since add() is not atomic.

## 4.1.2 - Implement a functional correctness test that finds concurrency errors in the remove(Integer element) method in ConcurrentIntegerSetBuggy. Describe the interleaving that your test finds.

One possible interleaving for test could be:

Given a set of [0,1,2,3]

t1(read set) -> t2(read set) -> t1 (remove '0') -> t2 (remove '0') 
-> t1(update size) -> t2(update size) 

So, size becomes 2 even though the same element is removed twice.

## 4.1.3 - In the class ConcurrentIntegerSetSync, implement fixes to the errors you found in the previous exercises. Run the tests again to increase your confidence that your updates fixed the problems. In addition, explain why your solution fixes the problems discovered by your tests.

All methods are now synchronized and thereby have instrinsic locks.
This also means that only add, remove or size operation can be executed at a time.
With this implementation, we simply ensure mutual exclusion meaning the size of the set
always will be correct.

## 4.1.4 - Run your tests on the ConcurrentIntegerSetLibrary. Discuss the results.

Our tests suceeded. Since ConcurrentIntegerSetLibrary is thread-safe and supports concurrent access from multiple threads, it makes perfectly sense that the tests suceed with this data structure.

## 4.1.5 - Do a failure on your tests above prove that the tested collection is not thread-safe? Explain your answer

Yes, as discussed in class: A class is said to be thread-safe if and only if
no concurrent execution of method calls or field accesses (read/write)
result in data races on the fields of the class.

If our tests on add() and remove() fail, we can conclude that the
tested collection is not thread safe.

## 4.1.6 - Does passing your tests above prove that the tested collection is thread-safe (when only using add() and remove())? Explain your answer

NO, even though add() and remove() passes both their respective tests,
we cannot know whether size() is also thread safe for sure. Hence, just because the tests passes, is does not prove whether or not the collection is actually thread-safe since we do not test the size method().

## 4.1.7 - It is possible that size() returns a value different than the actual number of elements in the set. Give an interleaving showing how this is possible

There is a implementation difference between HashSet and ConcurrentSkipListSet regarding the size method. Therefore, since it is constant time for HashSet it means that probably there is an internal field in the class that is being updated for every add or remove function call. In this case, if we can assure that both add and remove functions are synchronized with mutual exclusion and avoiding concurrent race condition, we can say that the value returned by size is reliable (using only add, remove and size), otherwise it is not because of the add and remove implementation (not synchronized - size continues working as expected). However, for the ConcurrentSkipListSet the implementation is asynchornous which means that the result of size is not reliable right after remove or add an element due to a need to traversal the set to expose the result. So, if an add or remove call happen during the traversal, it will output a wrong result.

Here is an interleaving showing the case where size returns a different value than expected using the ConcurrentSkipListSet class:

t1(size() - [J,K,D]) -> t2(add(E)) -> t1(size() - returns(3 instead of 4))

## 4.1.8 - Is it possible to write a test that detects the interleaving you provided in 7? Explain your answer

[ConcurrentSkipListSet] - We would say that it is possible to write a test that tries to detects the interleaving, being necessary many many (many...) repeated tests to detect it due to the timing it occurs and the non-deterministic thread schedule. Even though, it is not guaranteed that the test will detects the exact interleaving. In other words, it is not possible to write a reliable test for this case.

# 4.2

## 4.2.1 - Let capacity denote the final field capacity in SemaphoreImp. Then, the property above does not hold for SemaphoreImp. Your task is to provide an interleaving showing a counterexample of the property, and explain why the interleaving violates the property.

An interleaving that would violate the capacity property would be when a thread releases before a call to acquire since there is no control or check about negative numbers. In this way, it is possible to release many times before call acquire(), making the state a negative number allowing two many threads to enter the critical section thereby overexceeding the initial capacity we set. In other words, the state inside the Semaphore is implemented in a bad way allowing multiple threads to release without having acquired first.

Let's say that our capacity is 2 and we start 2 threads. Every thread will first call release and then call acquire. Afterwards, we start other 2 threads only calling to acquire the resource.

t1(release() [state = -1]) -> t2(release() [state = -2])
-> t1(acquire() [state = -1]) -> t2(acquire() [state = 0])
-> t3(acquire() [state = 1]) -> t4(acquire() [state = 2])

As we can see, there will be 4 threads in the critical section after this interleaving even if the capacity was set as 2.

## 4.2.2 - Write a functional correctness test that can trigger the interleaving you describe in 1. Explain why your test triggers the interleaving

The implemented tests checks the state of the threads when the capacity is supposed to be full. Therefore, the expected assertion would be with all created threads as WAITING state. Indeed, we can notice this behavior when we just call acquire() on each thread. However, when we call release() and the acquire(), all threads enter in the critical section and when asserting the state it will fail once it has a flag of TERMINATED. It is possible to check more details in the implementation.
