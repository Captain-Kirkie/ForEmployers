package lab03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class containerTest {
   // SortedSet<Integer> set = new TreeSet<>();
    public static void main(String[] args) {
        ArrayList<Double> avgList = new ArrayList<>();
        SortedSet<Integer> intList = new TreeSet<>();
        long startTime, stopTime, diff;
        double pow = 11;
        for (int r = 0; r < 10; r++) {
            ArrayList<Double> timeList = new ArrayList<Double>();
            double N = Math.pow(2, pow);
            for (int i = 0; i < N; i++) {
                int randInt = (int) (Math.random() * 1000);
                intList.add(randInt);
            }
            //collections.Contains
            //double input size
            //Start at 2^10 (1024) and end at 2^20 (1 million)
            //Collections.sort(intList); //sorts list
            startTime = System.nanoTime();
            while (System.nanoTime() - startTime < 1000000000) {
                //empty block
                //Wait for thread to stabalize
            }
            //now need to time it
            for (int k = 0; k < 5000; k++) {
                startTime = System.nanoTime();
                intList.contains(Math.random() * 1000);
                stopTime = System.nanoTime();
                diff = stopTime - startTime;
                timeList.add((double) diff);
            }
            double newAvg = Main.calculateAvg(timeList);
            avgList.add(newAvg);
            pow++;
        }
    }
}
