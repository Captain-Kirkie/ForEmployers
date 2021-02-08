package assignment03;

import java.util.ArrayList;
import java.util.List;

public class TimeTestAdd {


    public static void main(String[] args) {
        ArrayList<Double> avgList = new ArrayList<>();

        long startTime, stopTime, removeStartTime, removeStopTime;
//        Boolean added = false;
        for (int r = 11; r < 20; r++) { //how many averages to calculate

            ArrayList<Double> timeList = new ArrayList<Double>(); //reset the list
            BinarySearchSet<Double> testSet = new BinarySearchSet<Double>(); //create a binary search set
            startTime = System.nanoTime();
            while (System.nanoTime() - startTime < 1000000000) {
                //empty block
                //Wait for thread to stabalize
            }
            double N = Math.pow(2, r); //this will be the size of the collection

            for (double i = 0; i < N; i++) { //populate Test Set
                testSet.add(i);
            }
            double ToRemoveAndAdd = Math.random() * N;

            System.out.println("Size of N: " + N);
            double timesToLoop = 50000;
            startTime = System.nanoTime(); //time it
            double totalRemoveTime = 0;
            for (int k = 0; k < timesToLoop; k++){
                removeStartTime = System.nanoTime();
                testSet.remove(ToRemoveAndAdd);
                removeStopTime = System.nanoTime();
                totalRemoveTime += removeStopTime - removeStartTime;

                testSet.add(ToRemoveAndAdd);
            }


            double midPoint = System.nanoTime();

            for (int empty = 0; empty < timesToLoop; empty++) {
                //runs and does nothing
            }
            stopTime = System.nanoTime();
            double newAvg = (((midPoint - startTime) - totalRemoveTime) - (stopTime - midPoint)) / timesToLoop;
            System.out.println(newAvg);
            avgList.add(newAvg);
        }

        for (Double d : avgList) {
            System.out.println(d);
        }
    }

    public static double calculateAvg(List<Double> list) {
        Double sum = 0.0;
        if (!list.isEmpty()) {
            for (Double i : list) {
                sum += i;
            }
        }
        return sum / list.size();
    }

}
