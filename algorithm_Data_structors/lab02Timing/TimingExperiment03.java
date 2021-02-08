package lab02Timing;

public class TimingExperiment03 {

  public static void main(String[] args) {
    long lastTime = System.nanoTime();
    int advanceCount = 0;
    while (advanceCount < 100) {
      long currentTime = System.nanoTime();
      if (currentTime == lastTime){
        System.out.println("This is Never executed"); //never executed
        continue;
      }

      System.out.println("Time advanced " + (currentTime - lastTime) + " nanoseconds.");
      lastTime = currentTime;
      advanceCount++;
    }
  }
}



