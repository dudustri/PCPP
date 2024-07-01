//Exercise 10.?
//JSt vers Oct 23, 2023

//install  src/main/resources/english-words.txt
package exercises10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import benchmarking.Benchmark;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "src/main/resources/english-words.txt";

    
    //System.out.println("Read all words:");
    //System.out.println(readWords(filename));

    //System.out.println("\nPrint first hundreds words:");
    //printFirstHundredWords(filename);

    //System.out.println("\nPrint all words with at least 22 letters:");
    //printAll22Words(filename);

    //System.out.println("\nPrint some word with at least 22 letters:");
    //printSome22Word(filename);

    System.out.println("\nPrint all palindromes:");
    printPalindomes(filename);

    /* 
    Benchmark.Mark7("Stream", i -> {
      printPalindomes(filename);
      return 0;
    });
    */
    
    /*
    Benchmark.Mark7("Stream in parallel", i -> {
      printPalindomesParallel(filename);
      return 0;
    });
    */

    //System.out.println("\nRead words from URL: ");
    //System.out.println(readWordStreamURL("https://staunstrups.dk/jst/english-words.txt"));

    System.out.println("\nMinimum, maximum and average amount of words");
    minMaxAvgWords("https://staunstrups.dk/jst/english-words.txt");
  }
  public static long readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      long words = reader.lines()
      .filter( w -> w.length() > 1) //we the all lines with two or more characters followed by one another
      .filter( w -> Character.isAlphabetic(w.charAt(0))) // we do not really need this line, however, 
      // but maybe there could be a line with special-character(s) or number(s)?
      .count();
      return words;
    } catch (IOException exn) { 
        return -1;
    }
  }
 
  public static void printFirstHundredWords(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        AtomicInteger counter = new AtomicInteger(0); //we make an Atomic integer
        reader.lines() //get all lines
                .limit(100) //limit to first 100
                .forEach(w -> System.out.println(w + " " + counter.incrementAndGet()));
                //for each line we print it and add the counter next to it
    } catch (IOException exn) {
        exn.printStackTrace();
    }
  }

  public static void printAll22Words(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        AtomicInteger counter = new AtomicInteger(0); //we make an Atomic integer
        reader.lines() //get all lines
        .filter(w -> w.length() > 21) //get all words over 21 letters
        .forEach(w -> System.out.println(w + " " + w.length())); //print each word
    } catch (IOException exn) {
        exn.printStackTrace();
    }
  }

  public static void printSome22Word(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        AtomicInteger counter = new AtomicInteger(0); //we make an Atomic integer
        String word = reader.lines() //get all lines
          .filter(w -> w.length() > 21) //get all words over 21 letters
          .findAny()//findAny takes any element of stream -but seems to just pick the first?
          .orElse("no word found");//if no word found, print "no word found"
          System.out.println(word);
    } catch (IOException exn) {
        exn.printStackTrace();
    }
  }

  public static boolean isPalindrome(String s) {
    StringBuilder reverseBuilder = new StringBuilder(s).reverse();//we reverse the string
    String sReversed = reverseBuilder.toString(); //make it into a string
    boolean isStringPalindrome = s.equals(sReversed); //check if the reversed string equals the original
    return isStringPalindrome; //return the outcome
  }

  public static void printPalindomes(String filename){
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      reader.lines() //we get all the words/lines
      .forEach(w -> { 
        if(isPalindrome(w)) System.out.println(w); //we check every word whether it is palindome and print
      });
    } catch (IOException exn) {
        exn.printStackTrace();
    }
  }

  public static void printPalindomesParallel(String filename){
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      reader.lines()//get all the lines/words
      .parallel() //make it parallel
      .forEach(w -> { //for every word 
        if(isPalindrome(w)) System.out.println(w); //we check if it i palindrome
      });
    } catch (IOException exn) {
        exn.printStackTrace();
    }
  }

  public static long readWordStreamURL(String urlname){
    try {
      HttpURLConnection connection=
        (HttpURLConnection) new URL(urlname).openConnection();
      BufferedReader reader= 
          new BufferedReader(new InputStreamReader(connection.getInputStream())); 
      long words = reader.lines() // get all the lines/words
      .count(); // get the count
      return words;
    } catch (IOException exn) { return -1; }
  }

  public static void minMaxAvgWords(String urlname) {
    try {
        HttpURLConnection connection =
                (HttpURLConnection) new URL(urlname).openConnection();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));

        Stream<String> words = reader.lines(); // get all the words/lines
        DoubleSummaryStatistics stats = //we use DoubleSummaryStatistics
                words.mapToDouble(word -> word.length()) //return every word length as a double
                        .summaryStatistics(); // returns DoubleSummaryStatistics which all the info we need :-)

        System.out.printf("min=%g, max=%g, avg=%g",
                stats.getMin(), stats.getMax(), stats.getAverage()); // output all right info
    } catch (IOException exn) {
        exn.printStackTrace();
    }
}



  public static Map<Character,Integer> letters(String s) {
    Map<Character,Integer> res = new TreeMap<>();
    // TO DO: Implement properly
    return res;
  }
}
