package exercises07;
// raup@itu.dk * 05/10/2022
// jst@itu.dk * 23/9/2023

class Histogram1 implements Histogram {
  private int[] counts;

  public Histogram1(int span) {
    this.counts = new int[span];
  }

  public void increment(int bin) {
    counts[bin] = counts[bin] + 1;

  }

  public int getCount(int bin) {
    return counts[bin];
    
  }
  
  public int getSpan() {
    return counts.length;
  }

  public int getAndClear(int bin) {
    int currentVal = counts[bin];
    counts[bin] = 0;
    return currentVal;
  }
}
