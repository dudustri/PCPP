// sestoft@itu.dk * 2014-09-04
// thdy@itu.dk * 2019
// kasper@itu.dk * 2020
// raup@itu.dk * 05/10/2022
// jst@itu.dk * 23/9/2023
package exercise63;

public class SimpleHistogram {
  public static void main(String[] args) {
    final Histogram histogram = new Histogram1(30);
    histogram.increment(7);
    histogram.increment(13);
    histogram.increment(7);
    dump(histogram);
  }

  public static void dump(Histogram histogram) {
    for (int bin = 0; bin < histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
    }
  }
}


