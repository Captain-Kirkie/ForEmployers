package lab02Timing;

import java.util.ArrayList;

public class TimingExperiment07 {

  public static void main(String[] args) {
    ArrayList<Long> longArrayList = new ArrayList<>();
    for(int i = 0; i < 100; i++){
      long startTime = System.nanoTime();
      for (double d = 1; d <= 10; d++)
        Math.sqrt(d);
      long stopTime = System.nanoTime();
      long diff = stopTime - startTime;
      longArrayList.add(diff);
    }

    for(long l : longArrayList){
      System.out.println(l);
    }



//    System.out.println("It takes exactly " + (stopTime - startTime) + " nanoseconds to compute the square roots of the "
//        + " numbers 1..10.");
  }
}
