package exercises05;
// jst@itu.dk * 2023-09-05

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import benchmarking.Benchmark;
import benchmarking.Benchmarkable;

public class TestTimeSearch {
  public static void main(String[] args) {
    new TestTimeSearch();
  }

  public TestTimeSearch() {
    final String filename = "/Users/gustavmeding/Documents/SOFTWARE DESIGN/3rd semester/Practical Concurrent and Parallel Programming/PCPP_Exercises/Assignment 3/Exercise5/code-exercises/week05exercises/app/src/main/resources/long-text-file.txt";
    //change path
    final String target= "ipsum";

    final PrimeCounter lc = new PrimeCounter();  //name is a bit misleading, it is just a counter
    final PrimeCounter lcPar = new PrimeCounter();
    String[] lineArray= readWords(filename);

    System.out.println("Array Size: "+ lineArray.length);
    System.out.println("# (Seq) Occurences of "+target+ " :"+search(target, lineArray, 0, lineArray.length, lc));
    System.out.println("#(Par) Occurences of "+target+ " :"+countParallelN(target, lineArray,lineArray.length, lcPar));

    /*BenchMark */
    Benchmark.SystemInfo();
    System.out.println('\n' + "Benchmark sequential search:" + '\n');
    Benchmark.Mark7("search seq", i -> {
      return search(target, lineArray, 0, lineArray.length, lc);
    });


    System.out.println('\n' + "Benchmark Parallel search:" + '\n');
    Benchmark.Mark7("search par", i -> {
      return countParallelN(target, lineArray, 8, lc);
    });

  }

  static long search(String x, String[] lineArray, int from, int to, PrimeCounter lc){
    //Search each line of file
    for (int i=from; i<to; i++ ) {
      lc.add(linearSearch(x, lineArray[i]));
      //System.out.println("Found: "+lc.get());
    }
    return lc.get();
  }

  static int linearSearch(String x, String line) {
    //Search for occurences of c in line
    String[] arr= line.split(" ");
    int count= 0;
    for (int i=0; i<arr.length; i++ ) if ( (arr[i].equals(x)) ) count++;                   
    return count;
  }

  private static long countParallelN(String target, String[] lineArray, int N, PrimeCounter lcPar) {
      Thread[] threads = new Thread[N];//threads array
      int linesPerThread = lineArray.length / N; //we divide the whole text into parts for each threads

      //(we deleted AtomicLong counter and use PrimeCounter as prescribed instead)

      for (int i = 0; i < N; i++) { 
        final int startIndex = i * linesPerThread; //start index for given thread
        final int endIndex = (i == N - 1) ? lineArray.length : (i + 1) * linesPerThread; //end index for given thread - fx. 1000 lines (start: 0, end: 999)
        threads[i] = new Thread(() -> { //initiate the thread
          int threadCount = 0; //local counter
          for (int j = startIndex; j < endIndex; j++) {//count words within the given interval
            threadCount += linearSearch(target, lineArray[j]); //the threads searches the given block of text adds the amount target words in the given line
          }
          lcPar.add(threadCount);//add amount of target words found to shared variable
        });
      }

        for (int k = 0; k < N; k++) {
          threads[k].start();//start the threads
        }

        try {
          for (int l = 0; l < N; l++) {
            threads[l].join(); //join the result
          }
        } catch (InterruptedException e) {}
        
        return lcPar.get(); //return the final result
      }


  public static String[] readWords(String filename) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        return reader.lines().toArray(String[]::new);   //will be explained in Week07;
    } catch (IOException exn) { return null;}
  }

  
}
