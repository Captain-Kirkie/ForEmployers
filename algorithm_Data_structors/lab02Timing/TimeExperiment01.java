package lab02Timing;

public class TimeExperiment01 {

    public static void main(String[] args) {
        long lastTime = System.currentTimeMillis();
        int advanceCount = 0;
        while (advanceCount < 100) {
            long currentTime = System.currentTimeMillis();
            if (currentTime == lastTime){
                System.out.println("about to continue"); //slows things down a bunch
                continue;
            }
            System.out.println("Time advanced " + (currentTime - lastTime) + " milliseconds.");
            lastTime = currentTime;
            advanceCount++;
        }
        System.out.println(advanceCount);
        //continue does execute
    }
}
