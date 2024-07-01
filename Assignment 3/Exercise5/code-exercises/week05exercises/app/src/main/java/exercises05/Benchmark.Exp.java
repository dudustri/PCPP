package exercises05;
import benchmarking.Benchmark;

class BenchmarkExp {
  public static void main(String[] args) { new BenchmarkExp(); }
  
  public BenchmarkExp () {
    Benchmark.SystemInfo();
    Benchmark.Mark2();
  }
}