# Exercise 10

## Ex 10.1

### 10.1.1

Result from initial run(results are also in 'PrimeCountingPerf.txt'):

Sequential 9693801,4 ns 149448,91 32

### 10.1.2

See 'PrimeCountingPerf.java' and 'PrimeCountingPerf.txt'

After modifying IntStream method:

Sequential 9693801,4 ns 149448,91 32
IntStream 9882583,8 ns 720931,15 32

### 10.1.3

See 'PrimeCountingPerf.java'

### 10.1.4

See 'PrimeCountingPerf.java' and 'PrimeCountingPerf.txt'

Sequential 9693801,4 ns 149448,91 32
IntStream 9882583,8 ns 720931,15 32
Parallel 2945253,7 ns 516484,69 128

### 10.1.5

See 'PrimeCountingPerf.java' and 'PrimeCountingPerf.txt'

Sequential 9693801,4 ns 149448,91 32
IntStream 9882583,8 ns 720931,15 32
Parallel 2945253,7 ns 516484,69 128
ParallelStream 2741158,9 ns 292771,66 128

## 10.2

### 10.2.1

See 'TestWordStream.java'

### 10.2.2

See 'TestWordStream.java'

### 10.2.3

See 'TestWordStream.java'

### 10.2.4

See 'TestWordStream.java'

### 10.2.5

See 'TestWordStream.java'

Result:
Stream 19180273,6 ns 1055007,00 16
Stream in parallel 13331249,6 ns 320024,98 32

## 10.3

### 10.3.1

_For reference the following exercises was executed with the given characteristics:_
OS: Mac OS X; 13.0.1; x86_64
JVM: Homebrew; 20.0.1
CPU: null; 8 "cores"

_See 'Java8ParallelStreamMain.java'_
Result:

---

Using Sequential Stream

---

1 main
2 main
3 main
4 main
5 main
6 main
7 main
8 main
9 main
10 main

---

Using Parallel Stream

---

7 main
6 main
8 ForkJoinPool.commonPool-worker-2
3 ForkJoinPool.commonPool-worker-1
4 ForkJoinPool.commonPool-worker-1
10 ForkJoinPool.commonPool-worker-1
5 ForkJoinPool.commonPool-worker-2
2 ForkJoinPool.commonPool-worker-3
1 ForkJoinPool.commonPool-worker-2
9 main

_Explanation of output_:
As described in the article, the result above shows that whenever we use the 'regular' stream
(without .parallel()), the main thread does all the work. However, when we use .parallel()
four threads (main, 1,2,3) are spawned where each thread is managed by a ForkJoinPool.
Ultimately, this means that the parallel stream-method takes advantage of available (CPU) cores
to do the work.

### 10.3.2

If we double the sixe (20) we get a different result:

# ...

# Using Parallel Stream

13 main
15 main
7 ForkJoinPool.commonPool-worker-1
6 ForkJoinPool.commonPool-worker-1
14 main
9 ForkJoinPool.commonPool-worker-1
8 main
17 main
11 ForkJoinPool.commonPool-worker-5
12 ForkJoinPool.commonPool-worker-4
10 ForkJoinPool.commonPool-worker-1
3 ForkJoinPool.commonPool-worker-3
18 ForkJoinPool.commonPool-worker-2
16 main
2 ForkJoinPool.commonPool-worker-5
5 ForkJoinPool.commonPool-worker-6
20 ForkJoinPool.commonPool-worker-4
4 ForkJoinPool.commonPool-worker-7
1 ForkJoinPool.commonPool-worker-1
19 ForkJoinPool.commonPool-worker-3

As expected, when we increase the integer array, the parallel stream spawns threads
equal to the amount of CPU cores on our machine. The stream uses 8 threads (main, 1,2,3,4,5,6,7)
which is also equal to the amount of cores we have in total Thus, there seems to be correlation between
the number of spawned threads when using .parallel() and the number cores in our system.

### 10.3.3

Time consuming task: calculate prime factors for 10.000. 
Iterations: 100.000. 
Time taken to complete sequentially:40 seconds. 

Time taken to complete parallel:9 seconds

### 10.4.1 & 10.4.2

The exercise explores the differences between Java Streams and RxJava
through the context of a given pseudo-code involving source(), filter(),
and sink() operations.

**Java Streams**
Scenario 1:
source().filter(w -> w.length() > 5).sink()
If the source is a file for example, the stream of words from the file is created.
This operation is lazy and the filtering process happens only when the terminal operation (sink()) is triggered.
So then the Stream can "produce" more data to handled by the stream operations.

Scenario 2:
source().filter(w -> w.length() > 5).sink();
source().filter(w -> w.length() > 10).sink()
In this case each stream is independent, and the operations on one stream do not affect the other.

**RxJava**
Scenario 1:
source().filter(w -> w.length() > 5).sink()

Here, if the source is an input field where a user types strings, source()
represents an observable that emits items (words) as they are entered.
Unlike Java Streams, RxJava is push-based and asynchronous. The sink() in RxJava would be a
subscriber that reacts to the filtered stream in real-time, responding to each item as it comes.

Scenario 2:
source().filter(w -> w.length() > 5).sink();
source().filter(w -> w.length() > 10).sink()
Here each observable operates independently as in a JavaStream, and the processing is reactive and event-driven.
So it happens only when the source emits an item.

FEEDBACK: The difference is that in RxJava you pull from the same source.
But we got the key point.
