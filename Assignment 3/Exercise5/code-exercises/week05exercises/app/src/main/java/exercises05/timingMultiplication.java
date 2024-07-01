package exercises05;
// Timing multiplication taken from BenchMark.java
// Code to be used in class05, updated by jst@itu.dk 09/08/2022
import benchmarking.Benchmark;
import benchmarking.Benchmarkable;
import benchmarking.Timer;

class timingMultiplication {
  public static void main(String[] args) { new timingMultiplication(); }
  
  public timingMultiplication() {
    Benchmark.SystemInfo();
    Benchmark.Mark2();
  }
}
