#### SystemInfo
Gustav:  
    OS: Mac OS X; 13.0.1; x86_64
    JVM: Oracle Corporation; 20.0.2
    CPU: null; 8 "procs"
    Date: 2023-09-27T21:22:18+0200


# Exercise 5

## Ex 5.1
### Ex 5.1.1
- ...

### Ex 5.1.2
- Object creation is approx. 37 ns which is naturally very similar to lecture results.
Thread creation is 918 ns which is also very similar to the result in the lecture.
Thread create + start is 63510 ns which clearly shows that a lot of work goes into creating and starting a thread as we supposed.

Gustav results:

# OS:   Mac OS X; 13.0.1; x86_64
# JVM:  Homebrew; 20.0.1
# CPU:  1,4 GHz Quad-Core Intel Core i5; 8 "cores"
# Date: 2023-09-27T21:43:53+0200
Mark 7 measurements
hashCode()                            2,5 ns       0,00  134217728
Point creation                       38,6 ns       0,12    8388608
Thread's work                      4922,9 ns       8,99      65536
Thread create                       918,4 ns      35,00     524288
Thread create start               63510,8 ns    4059,73       4096
Thread create start join          80890,4 ns    4504,68       4096
ai value = 1474500000
Uncontended lock                     18,0 ns       0,26   16777216

## Ex 5.2
### Ex 5.2.2
We went with 16 threads since 32 took too long.
Yes, the results are in fact very plausible. Firstly, as the number of threads increase 
the exectution time generally decreases indicating that we are utilizing multiple cores.
However, after 8 threads the execution time starts to increase slightly which again
indicates that we are the execution is highly affected by our CPU 'only' having 8 cores.
Thus, we can say that there is a reasonable relation between the CPU and the number of threads executed.

### Ex. 5.2.3
Indeed, the code is now faster. Generally we see that even after 8 threads the exectution time still drops where the lowest is at 12 threads.

## Ex 5.3
See 'TestVolatile.java' for implementation and 'TestVolatile_Results.txt for results.

The results we get from our implementation of TestVolatile are indeed very plausible.
The incrementation of the normal integer is approx. 2.75 times faster than the incremenation of the volatile integer. This is because in general volatile variables, like vCtr, have additional overhead due to memory synchronization ultimately making them slower than non-volatile variables. So in short when making variables via the 'volatile'-keyword we make sure that they are visible to all instances of the given class, however, we also pay for it in execution time.

## Ex 5.4
### Ex 5.4.1
..
### Ex 5.4.2
Array Size: 5697
Occurences of ipsum : 1430

### Ex 5.4.3
Benchmark sequential search:

search seq                     11316118,2 ns  302388,54         32

### Ex. 5.4.4
...
### Ex. 5.4.5

OS:   Mac OS X; 13.0.1; x86_64
JVM:  Oracle Corporation; 20.0.2
CPU:  null; 8 "cores"
Date: 2023-10-08T11:38:55+0200


Benchmark sequential search:

search seq                     16650603,3 ns  120462,62         16

Benchmark Parallel search:

search par                      4061240,1 ns  118070,44         64

As seen above, the parallel solution is approx. four times faster than
the sequential one. The outcome is very plausible since we start 8 threads 
at once where every thread is accountable for each its own portion of the entire text.
Thus, naturally the parallel will execute faster.







