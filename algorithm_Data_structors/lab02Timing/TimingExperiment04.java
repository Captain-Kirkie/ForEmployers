package lab02Timing;

public class TimingExperiment04 {

  public static void main(String[] args) {
    long lastTime = System.nanoTime(); //gets the time
    int advanceCount = 0;
    long[] advanceAmounts = new long[100];
    while (advanceCount < 100) {
      long currentTime = System.nanoTime(); //gets current time
      if (currentTime == lastTime){ //if these two are equal exacutes, but wont
        System.out.println("Hey Kirk!"); //continue statement never executed
        continue;
      }
      //stored in array and printed out after
      advanceAmounts[advanceCount++] = currentTime - lastTime; //store the difference in an array
      lastTime = currentTime; //update lastTime
    }
    for (int i = 0; i < 100; i++)
      System.out.println("Time advanced " + advanceAmounts[i] + " nanoseconds.");
  }
}
