package lab02Timing;

public class TimingExperiment02 {

  public static void main(String[] args) {
    long secondsToRun = 10;
    long startTime = System.currentTimeMillis(); //initial start time
    long lastTime = startTime; //set the last time equal to start time
    int advanceCount = 0;
    int loopCount = 0; //how many times does loop run
    while (lastTime - startTime < 1000 * secondsToRun) {
      loopCount++;
      long currentTime = System.currentTimeMillis();
      if (currentTime == lastTime)
        continue;
      lastTime = currentTime;
      advanceCount++;
    }
    double advancesPerSecond = advanceCount / (double) secondsToRun;
    System.out.println("Time advanced " + advancesPerSecond + " times per second.");
    System.out.println("The loop tested the time " + loopCount + " times.");
  }
}

/*
About 1000 times pers second with one second
About 1000 times per second with 10 seconds

 */