# Exercise 6

## Ex 6.1
### Ex 6.1.1

_Use Mark7 (from Benchmark.java in the benchmarking package) to measure the execution time and verify that the time it takes to run the program is proportional to the transaction time._

- We included the Mark7 class in the AccountExperiments class as follows:
  `Benchmark.Mark7("doNTransactions", i -> doNTransactions(NO_TRANSACTION));`

- Then we ran the programm with 5, 10, 20, 40, 80 `NO_TRANSACTION`
- The results are as follows:

| NO_TRANSACTION | Avg Execution Time (ns) | Std Deviation (ns) | Count |
| -------------- | ----------------------- | ------------------ | ----- |
| 5              | 262529906.0             | 4606077.58         | 2     |
| 10             | 529229614,7             | 763648,56          | 2     |
| 20             | 1059155817,2            | 4080718,15         | 2     |
| 40             | 2115994329,2            | 11557415,60        | 2     |

- the results show that the execution time is proportional to the transaction time,
  as the number of transactions doubles, the execution time doubles as well.

### Ex 6.1.2

_Explain why the calculation of min and max are necessary? Eg. what could happen if the code was written like this:_

- The calculation of min and max are necessary to prevent deadlocks. By using the `min` and `max` calculation, the code ensures that the locks are always acquired in the same order,
  regardless of the direction of the transfer. If we wouldn't use the `min` and `max` calculation and use the following lines provided:

```java
Account s= accounts[source.id];
Account t = accounts[target.id];
synchronized(s){
    synchronized(t){
```

- Following scenario could happen:

  - Thread A: `transfer(1,2,10)`  (amount, source, target)
  - Thread B: `transfer(2,1,10)`
  - Thread A locks account 1.
  - Almost simultaneously, Thread B locks account 2.
  - Thread A tries to lock account 2 for the transfer but can't because Thread B has it locked.
  - Thread B tries to lock account 1 but can't because Thread A has it locked.
  - Both threads are now waiting for the other to release the lock, which will never happen.

- By using the `min` and `max` calculation, the code ensures that the locks are always acquired in the same order. Running
  the program with the `min` and `max` calculation, we can see that the program runs without any errors. If we run the
  program without the `min` and `max` calculation, there might be the chance that the program runs into a deadlock.

### Ex 6.1.3

- please see the code in the 'ForkJoinPoolAccountExperimentsMany' class in the 'ForkJoinPoolAccountExperimentsMany' file.

### Ex 6.1.4

- please see the code in the 'ForkJoinPoolAccountExperimentsMany' class in the 'ForkJoinPoolAccountExperimentsMany' file.

### Ex 6.2

Taking a look and understanding the code  would be possible to notice that we are comparing the execution time, standard deviation and number of iterations between the countSequential (in sequence - no threads), the parallel implementation (countParallelN) using an atomic long (sharing the resource), and the parallel implementation using an auxiliary array saving the result based on the index of the thread (storing locally (no locks) - countParallelNLocal) and summing the results after all threads join the code (divide and conquer). Both parallel methods were implemented to run using 1 to 32 threads. The output we had after running the code is the following:

| Test                             | Mean Time (ns) | Standard Deviation (ns) | Iterations |
| -------------------------------- | -------------- | ----------------------- | ---------- |
| countSequential                  | 5,423,550.0    | 57,117.95               | 64         |
| countParallelN (1 thread)        | 5,533,594.7    | 111,485.57              | 64         |
| countParallelNLocal (1 thread)   | 5,455,743.8    | 70,823.65               | 64         |
| countParallelN (2 threads)       | 4,000,324.0    | 54,319.20               | 64         |
| countParallelNLocal (2 threads)  | 3,951,092.4    | 57,344.99               | 64         |
| countParallelN (3 threads)       | 3,273,621.0    | 50,733.08               | 128        |
| countParallelNLocal (3 threads)  | 3,268,906.9    | 138,102.89              | 128        |
**| countParallelN (4 threads)     | 2,868,378.5    | 127,639.23              | 128        |**
**| countParallelNLocal (4 threads)| 2,815,249.4    | 119,696.93              | 128        |**
| countParallelN (5 threads)       | 2,868,561.7    | 43,998.29               | 128        |
| countParallelNLocal (5 threads)  | 2,853,561.5    | 30,141.22               | 128        |
| countParallelN (6 threads)       | 2,895,638.8    | 39,004.59               | 128        |
| countParallelNLocal (6 threads)  | 3,973,081.0    | 478,486.47              | 128        |
| countParallelN (7 threads)       | 4,007,496.1    | 287,172.83              | 64         |
| countParallelNLocal (7 threads)  | 3,047,829.9    | 70,738.08               | 128        |
| countParallelN (8 threads)       | 3,376,787.9    | 148,435.03              | 128        |
| countParallelNLocal (8 threads)  | 3,189,065.7    | 52,323.95               | 128        |
| countParallelN (9 threads)       | 3,331,550.5    | 153,651.59              | 128        |
| countParallelNLocal (9 threads)  | 3,234,446.6    | 66,358.31               | 128        |
| countParallelN (10 threads)      | 3,331,379.2    | 41,720.50               | 128        |
| countParallelNLocal (10 threads) | 3,142,876.4    | 169,199.24              | 128        |
| countParallelN (11 threads)      | 3,409,167.4    | 86,845.30               | 128        |
| countParallelNLocal (11 threads) | 3,709,727.2    | 321,392.33              | 128        |
| countParallelN (12 threads)      | 3,772,271.0    | 256,048.69              | 128        |
| countParallelNLocal (12 threads) | 3,689,689.4    | 178,368.91              | 128        |
| countParallelN (13 threads)      | 3,602,999.3    | 59,451.75               | 128        |
| countParallelNLocal (13 threads) | 3,934,795.8    | 331,047.48              | 128        |
| countParallelN (14 threads)      | 3,986,155.1    | 194,573.27              | 128        |
| countParallelNLocal (14 threads) | 3,698,842.4    | 220,998.60              | 128        |
| countParallelN (15 threads)      | 3,988,015.5    | 305,541.01              | 128        |
| countParallelNLocal (15 threads) | 3,754,944.4    | 165,969.46              | 128        |
| countParallelN (16 threads)      | 4,021,958.1    | 251,859.38              | 128        |
| countParallelNLocal (16 threads) | 4,484,604.3    | 475,491.15              | 64         |
| countParallelN (17 threads)      | 4,570,257.1    | 229,914.55              | 64         |
| countParallelNLocal (17 threads) | 4,018,709.0    | 266,670.53              | 64         |
| countParallelN (18 threads)      | 4,123,203.7    | 90,458.86               | 64         |
| countParallelNLocal (18 threads) | 4,110,868.4    | 80,707.01               | 64         |
| countParallelN (19 threads)      | 4,470,533.2    | 373,479.09              | 64         |
| countParallelNLocal (19 threads) | 4,281,879.1    | 109,570.53              | 64         |
| countParallelN (20 threads)      | 4,404,231.4    | 126,206.22              | 64         |
| countParallelNLocal (20 threads) | 4,367,554.2    | 84,230.12               | 64         |
| countParallelN (21 threads)      | 4,954,182.3    | 607,006.56              | 64         |
| countParallelNLocal (21 threads) | 4,499,077.2    | 45,855.72               | 64         |
| countParallelN (22 threads)      | 4,689,541.1    | 70,935.88               | 64         |
| countParallelNLocal (22 threads) | 4,891,807.3    | 374,523.16              | 64         |
| countParallelN (23 threads)      | 4,825,545.3    | 100,020.13              | 64         |
| countParallelNLocal (23 threads) | 4,781,483.7    | 48,193.99               | 64         |
| countParallelN (24 threads)      | 4,943,257.4    | 95,822.72               | 64         |
| countParallelNLocal (24 threads) | 4,954,775.7    | 81,839.21               | 64         |
| countParallelN (25 threads)      | 5,084,106.2    | 103,265.65              | 64         |
| countParallelNLocal (25 threads) | 5,054,469.0    | 68,540.60               | 64         |
| countParallelN (26 threads)      | 5,247,432.2    | 99,603.57               | 64         |
| countParallelNLocal (26 threads) | 5,238,659.7    | 96,143.78               | 64         |
| countParallelN (27 threads)      | 6,436,676.2    | 405,929.13              | 64         |
| countParallelNLocal (27 threads) | 5,748,115.6    | 286,940.96              | 64         |
| countParallelN (28 threads)      | 6,060,788.7    | 274,366.02              | 64         |
| countParallelNLocal (28 threads) | 5,497,494.6    | 85,251.33               | 64         |
| countParallelN (29 threads)      | 5,654,155.3    | 60,941.79               | 64         |
| countParallelNLocal (29 threads) | 6,049,547.0    | 649,594.11              | 64         |
| countParallelN (30 threads)      | 5,813,572.9    | 34,536.22               | 64         |
| countParallelNLocal (30 threads) | 5,779,628.0    | 54,120.43               | 64         |
| countParallelN (31 threads)      | 5,931,757.2    | 73,944.01               | 64         |
| countParallelNLocal (31 threads) | 5,981,573.4    | 92,723.63               | 64         |
| countParallelN (32 threads)      | 6,106,805.4    | 98,192.58               | 64         |
| countParallelNLocal (32 threads) | 6,082,071.0    | 110,895.23              | 64         |

Those results are returned when running in one of our computers, and, since it is dependent from the hardware specifications we cannot assure that the results will be accurate for all machines. However, based on this results its possible to determine that there is an optimal range when talking about number of threads. The results presented shown that **from 2 to 26 threads the parallel implementation is indeed faster than the sequential one. Nevertheless, if we cross this number the execution results are slower than the sequential one. In detail, the "optimal" number of threads for the machine specification where the code was ran is of 4 threads. The execution mean time drops until this number of threads and then start to incresce again after it.**

In resume, the parallel implementation can increase significantly the execution time performance of the method. However, more threads doesn't mean more performance once it execution time increase after reach certain number of threads (4 in our case). Also, this may vary depending on the hardware being used. In addition, it's possible to see that the countParallelNLocal tends to have a slightly lower execution mean time when compared with the countParallelN, probably because the countParallelNLocal don't need to care about sharing resources while executing the threads.

### 6.2.2

We Implemented two new methods following the logic of the previous implementation but using Future and ForkJoinPool. It is possible to check in the TestCountPrimesThreads.java file. Also, we set different colors when the strings are being printed in the terminal to make easier to identify the results. Here are the results from the execution:

| Method                            | Mean Time (ns) | Standard Deviation | Thread Count |
| --------------------------------- | -------------- | ------------------ | ------------ |
| countSequential                   | 6,464,173.2    | 1,988,133.92       | 64           |
| countParallelN 1                  | 7,222,164.7    | 1,541,063.95       | 64           |
| countParallelNLocal 1             | 6,259,418.6    | 197,918.81         | 64           |
| countParallelNWithFutures 1       | 6,911,567.9    | 373,113.34         | 64           |
| countParallelNLocalWithFutures 1  | 6,335,968.5    | 178,120.35         | 64           |
| countParallelN 2                  | 4,393,368.6    | 119,939.45         | 64           |
| countParallelNLocal 2             | 4,425,854.9    | 244,885.76         | 64           |
| countParallelNWithFutures 2       | 4,731,500.5    | 84,327.60          | 64           |
| countParallelNLocalWithFutures 2  | 4,583,487.7    | 74,031.23          | 64           |
| countParallelN 3                  | 3,580,106.8    | 82,731.90          | 128          |
| countParallelNLocal 3             | 3,592,528.3    | 194,288.14         | 128          |
| countParallelNWithFutures 3       | 4,507,623.6    | 317,117.56         | 64           |
| countParallelNLocalWithFutures 3  | 4,343,090.8    | 51,044.70          | 64           |
| countParallelN 4                  | 3,966,573.9    | 179,991.14         | 128          |
| countParallelNLocal 4             | 3,775,378.5    | 134,508.23         | 128          |
| countParallelNWithFutures 4       | 4,437,713.0    | 211,037.05         | 64           |
| countParallelNLocalWithFutures 4  | 4,308,371.1    | 150,829.67         | 64           |
| countParallelN 5                  | 4,581,578.4    | 1,214,342.14       | 128          |
| countParallelNLocal 5             | 3,594,479.6    | 104,183.72         | 128          |
| countParallelNWithFutures 5       | 4,704,308.9    | 74,332.46          | 64           |
| countParallelNLocalWithFutures 5  | 4,519,973.0    | 220,170.56         | 128          |
| countParallelN 6                  | 3,499,874.6    | 295,131.96         | 128          |
| countParallelNLocal 6             | 3,366,801.4    | 56,609.65          | 128          |
| countParallelNWithFutures 6       | 3,673,331.1    | 425,785.67         | 64           |
| countParallelNLocalWithFutures 6  | 4,211,641.7    | 521,845.02         | 64           |
| countParallelN 7                  | 3,157,122.7    | 430,760.74         | 128          |
| countParallelNLocal 7             | 3,151,834.5    | 260,163.12         | 128          |
| countParallelNWithFutures 7       | 3,223,703.9    | 134,581.89         | 128          |
| countParallelNLocalWithFutures 7  | 3,410,272.0    | 236,306.81         | 128          |
**| countParallelN 8                | 2,957,864.9    | 282,273.37         | 128          |**
| countParallelNLocal 8             | 3,042,938.9    | 425,490.95         | 128          |
**| countParallelNWithFutures 8     | 3,174,616.4    | 153,174.82         | 128          |**
**| countParallelNLocalWithFutures 8| 3,293,949.8    | 112,212.68         | 128          |**
| countParallelN 9                  | 3,381,671.0    | 543,036.33         | 128          |
**| countParallelNLocal 9           | 2,823,463.1    | 194,650.30         | 128          |**
| countParallelNWithFutures 9       | 3,361,208.0    | 215,922.57         | 128          |
| countParallelNLocalWithFutures 9  | 3,458,818.4    | 128,056.31         | 128          |
| countParallelN 10                 | 3,410,614.8    | 509,128.71         | 128          |
| countParallelNLocal 10            | 2,980,183.2    | 294,039.04         | 128          |
| countParallelNWithFutures 10      | 3,368,705.0    | 172,964.76         | 128          |
| countParallelNLocalWithFutures 10 | 3,509,558.4    | 151,272.39         | 128          |
| countParallelN 11                 | 2,997,161.8    | 143,698.29         | 128          |
| countParallelNLocal 11            | 2,893,117.5    | 16,811.84          | 128          |
| countParallelNWithFutures 11      | 3,435,961.4    | 125,675.38         | 128          |
| countParallelNLocalWithFutures 11 | 3,886,099.8    | 542,402.69         | 128          |
| countParallelN 12                 | 3,046,202.2    | 210,831.44         | 128          |
| countParallelNLocal 12            | 3,067,876.3    | 219,073.66         | 128          |
| countParallelNWithFutures 12      | 3,613,102.5    | 198,762.23         | 128          |
| countParallelNLocalWithFutures 12 | 3,712,603.2    | 186,510.41         | 128          |
| countParallelN 13                 | 3,044,305.2    | 64,508.10          | 128          |
| countParallelNLocal 13            | 3,133,072.1    | 211,860.77         | 128          |
| countParallelNWithFutures 13      | 3,982,648.6    | 593,460.32         | 64           |
| countParallelNLocalWithFutures 13 | 4,089,514.8    | 383,427.97         | 128          |
| countParallelN 14                 | 3,821,010.4    | 428,577.31         | 128          |
| countParallelNLocal 14            | 3,204,008.5    | 170,750.64         | 128          |
| countParallelNWithFutures 14      | 3,765,684.6    | 182,815.93         | 128          |
| countParallelNLocalWithFutures 14 | 3,852,320.1    | 196,973.05         | 64           |
| countParallelN 15                 | 3,242,859.7    | 173,008.22         | 128          |
| countParallelNLocal 15            | 3,286,057.5    | 196,394.90         | 128          |
| countParallelNWithFutures 15      | 3,825,426.0    | 143,418.45         | 128          |
| countParallelNLocalWithFutures 15 | 3,916,598.6    | 121,978.75         | 128          |
| countParallelN 16                 | 3,379,011.0    | 340,539.19         | 128          |
| countParallelNLocal 16            | 3,546,126.5    | 218,471.17         | 128          |
| countParallelNWithFutures 16      | 3,883,726.8    | 121,451.86         | 128          |
| countParallelNLocalWithFutures 16 | 4,042,994.8    | 374,298.09         | 64           |
| countParallelN 17                 | 3,387,863.7    | 120,383.80         | 128          |
| countParallelNLocal 17            | 3,402,336.7    | 107,894.18         | 128          |
| countParallelNWithFutures 17      | 4,140,099.9    | 564,659.76         | 64           |
| countParallelNLocalWithFutures 17 | 4,108,458.8    | 231,830.82         | 64           |
| countParallelN 18                 | 3,503,685.9    | 184,054.31         | 128          |
| countParallelNLocal 18            | 3,407,960.7    | 33,801.19          | 128          |
| countParallelNWithFutures 18      | 4,017,061.1    | 80,032.35          | 64           |
| countParallelNLocalWithFutures 18 | 4,111,300.8    | 223,459.30         | 64           |
| countParallelN 19                 | 3,581,119.7    | 187,909.63         | 128          |
| countParallelNLocal 19            | 3,558,150.1    | 158,668.03         | 128          |
| countParallelNWithFutures 19      | 4,137,596.4    | 175,344.98         | 64           |
| countParallelNLocalWithFutures 19 | 4,175,956.2    | 98,321.82          | 64           |
| countParallelN 20                 | 3,677,549.5    | 118,136.93         | 128          |
| countParallelNLocal 20            | 3,797,412.0    | 230,079.20         | 128          |
| countParallelNWithFutures 20      | 4,052,918.5    | 48,638.60          | 64           |
| countParallelNLocalWithFutures 20 | 4,250,132.9    | 49,754.60          | 64           |
| countParallelN 21                 | 3,907,806.9    | 339,236.49         | 64           |
| countParallelNLocal 21            | 4,190,501.1    | 474,061.61         | 128          |
| countParallelNWithFutures 21      | 4,647,157.2    | 749,780.63         | 64           |
| countParallelNLocalWithFutures 21 | 4,372,872.9    | 212,327.94         | 64           |
| countParallelN 22                 | 4,008,994.6    | 251,100.71         | 64           |
| countParallelNLocal 22            | 3,973,548.9    | 224,604.65         | 64           |
| countParallelNWithFutures 22      | 4,331,819.1    | 327,916.82         | 64           |
| countParallelNLocalWithFutures 22 | 4,347,284.4    | 186,866.11         | 64           |
| countParallelN 23                 | 4,059,291.4    | 294,939.97         | 64           |
| countParallelNLocal 23            | 4,179,316.1    | 293,211.56         | 64           |
| countParallelNWithFutures 23      | 4,349,837.1    | 286,364.30         | 64           |
| countParallelNLocalWithFutures 23 | 4,456,846.8    | 305,588.07         | 64           |
| countParallelN 24                 | 4,610,019.2    | 778,810.23         | 64           |
| countParallelNLocal 24            | 4,365,487.3    | 229,866.36         | 64           |
| countParallelNWithFutures 24      | 4,495,529.2    | 187,494.05         | 64           |
| countParallelNLocalWithFutures 24 | 5,889,288.3    | 613,667.63         | 64           |
| countParallelN 25                 | 5,400,858.7    | 995,682.33         | 64           |
| countParallelNLocal 25            | 6,104,121.8    | 2,288,029.38       | 32           |
| countParallelNWithFutures 25      | 6,296,826.0    | 1,281,025.33       | 64           |
| countParallelNLocalWithFutures 25 | 6,554,039.6    | 2,003,021.39       | 64           |
| countParallelN 26                 | 6,978,854.5    | 2,735,312.27       | 64           |
| countParallelNLocal 26            | 7,562,041.7    | 3,477,230.60       | 32           |
| countParallelNWithFutures 26      | 6,067,982.6    | 1,032,373.45       | 64           |
| countParallelNLocalWithFutures 26 | 7,320,048.4    | 1,171,362.78       | 64           |
| countParallelN 27                 | 6,783,903.8    | 2,434,714.71       | 32           |
| countParallelNLocal 27            | 8,725,689.0    | 4,203,823.21       | 32           |
| countParallelNWithFutures 27      | 5,940,108.1    | 1,935,533.93       | 32           |
| countParallelNLocalWithFutures 27 | 5,034,728.3    | 258,044.73         | 64           |
| countParallelN 28                 | 9,774,965.7    | 1,920,044.33       | 32           |
| countParallelNLocal 28            | 7,291,986.0    | 2,696,074.89       | 64           |
| countParallelNWithFutures 28      | 6,294,175.9    | 2,008,509.35       | 32           |
| countParallelNLocalWithFutures 28 | 4,956,470.9    | 350,621.58         | 64           |
| countParallelN 29                 | 5,315,734.5    | 699,113.99         | 64           |
| countParallelNLocal 29            | 5,627,791.3    | 812,645.78         | 64           |
| countParallelNWithFutures 29      | 5,411,155.2    | 773,906.60         | 64           |
| countParallelNLocalWithFutures 29 | 4,688,629.8    | 151,327.92         | 64           |
| countParallelN 30                 | 5,181,269.0    | 594,579.97         | 64           |
| countParallelNLocal 30            | 5,262,440.3    | 640,356.27         | 64           |
| countParallelNWithFutures 30      | 4,738,784.4    | 407,955.48         | 64           |
| countParallelNLocalWithFutures 30 | 4,726,179.9    | 78,610.58          | 64           |
| countParallelN 31                 | 5,534,854.2    | 1,052,757.25       | 64           |
| countParallelNLocal 31            | 5,428,179.8    | 706,379.96         | 64           |
| countParallelNWithFutures 31      | 4,679,266.1    | 80,422.60          | 64           |
| countParallelNLocalWithFutures 31 | 4,812,710.7    | 122,139.99         | 64           |
| countParallelN 32                 | 5,331,608.8    | 494,096.57         | 64           |
| countParallelNLocal 32            | 5,483,774.7    | 563,233.52         | 64           |
| countParallelNWithFutures 32      | 4,654,302.5    | 118,048.83         | 64           |
| countParallelNLocalWithFutures 32 | 4,897,799.9    | 193,270.44         | 64           |

Analysing the results, our performance evaluation of prime number counting methods revealed something we weren't expecting in the end. When varying the number of threads, we observed that increasing the thread count generally can led to a reduced execution time but not always (optimal number of threads), as describe in 6.2.1. Surprisingly, a notable observation was the _consistently slower_ execution time of the methods that were implemented with ForkJoinPool. This outcome was unexpected considering the typical advantages of task parallelism. These results show the importance of selecting the most suitable parallelization approach based on the task requirements to get optimal performance.

## Ex 6.3

### Ex 6.3.1

_Make a thread-safe implementation, class Histogram2 implementing the interface Histogram_

- please see the code in the `Histogram2` class in the `Histogram2` file.

_Explain what fields and methods need modifiers and why. Does the getSpan method need to be synchronized?_

- The array `counts` needs the keyword final to ensure that the reference to the array canot
  be changed after the constructor has initialized it.
- **Synchronized Methods:** Both increment and getCount methods are marked as synchronized.
  This ensures that no two threads can execute these methods at the same time for the same Histogram2 object,
  providing mutual exclusion for the shared state which is the counts array.
- There no Locks for the `getSpan` method, because it is
  a read-only method and does not change the state of the object.

### Ex 6.3.2

- please see the code in the `Histogram3` class in the `Histogram3` file.

### Ex 6.3.3

- please see the code in the `HistogramPrimesThreads`class in the `HistogramPrimesThreads` file.

### Ex.6.3.4

_Evaluate the effect of lock striping on the performance of question 6.3._

- please see the code in the `LockStripingEvaluation` class in the `LockStripingEvaluation` file.

_Report your results and comment on them. Is there a increase
or not? Why?_

- Results of lock striping on the performance of the Histogram3 class:

| Description              | Time (ns)       | Deviation (ns) | Count |
| ------------------------ | --------------- | -------------- | ----- |
| Histogram3 with 1 locks  | 2,643,844,030.1 | 120,713,706.35 | 2     |
| Histogram3 with 2 locks  | 2,048,227,431.8 | 109,398,377.45 | 2     |
| Histogram3 with 4 locks  | 1,656,222,608.3 | 47,421,404.57  | 2     |
| Histogram3 with 8 locks  | 1,511,509,441.7 | 43,983,737.35  | 2     |
| Histogram3 with 16 locks | 1,614,329,802.5 | 75,433,847.40  | 2     |

- In our experiment we doubled the number of locks each time from 1 to 16. Using
  a single lock results in the worst performance (2.64ns) because all threads have to wait for the same lock.
  The best performance is achieved with 8 locks (1.51ns), in this case the threads are more likely to hit different locks.
  However increasing the number of locks further to 16 results in a worse performance (1.61ns). While increasing the number
  of locks may initially reduce contention (and thus speed up the execution), 
  **after a certain point, the benefits of reduced contention can be outweighed by the overhead of managing more locks. Overhead in this case means that each lock needs to be managed, checked, potentially waited upon, and released in the end. The "sweet spot" in our experiment appears to be using 8 locks.***
