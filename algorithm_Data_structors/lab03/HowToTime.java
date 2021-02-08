package lab03;

public class HowToTime {
  public static final int BILLION = 1000_000_000;
  public static final int MILLION = 1_000_000;

  public static void main(String[] args) {

    // countToABillionInYourHead();
    countToABillionOutLoud();
    // countToABillionOnYourFingers();
  }

  public static void countToABillionInYourHead() {
    int count = 0;
    long start;
    start = System.nanoTime();
    for (; count < Integer.MAX_VALUE; count++)
      ;
    long end = System.nanoTime() - start;
    double time = end / (double) BILLION;
    System.out.println("I counted to " + count + " in " + time + " seconds");
  }

  public static void countToABillionOutLoud() {
    int count = 0;
    long start;
    start = System.nanoTime();
    for (; count < Integer.MAX_VALUE; count++) {
      System.out.println(count);
    }
    long end = System.nanoTime() - start;
    double time = end / (double) BILLION;
    System.out.println("I counted to " + count + " in " + time + " seconds");
  }

  public static void countToABillionOnYourFingers() {
    int count = 0;
    long start;
    int size = BILLION;
    System.out.println("Size: " + size);
    boolean[] fingers = new boolean[size];
    start = System.nanoTime();
    for (; count < size; count++) {
      fingers[count] = true;
    }
    long end = System.nanoTime() - start;
    double time = end / (double) BILLION;
    for (boolean finger : fingers) {
      if (!finger) {
        System.out.println("Woops! Missed a finger");
      }
    }
    System.out.println("I counted to " + count + " in " + time + " seconds... on my fingers!");
  }
}
