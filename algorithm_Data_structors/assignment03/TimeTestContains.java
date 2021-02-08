package assignment03;

import lab03.Main;

import java.util.ArrayList;
import java.util.List;

public class TimeTestContains { //TODO: TEST CONTAINS,
    //make new BinarySearchSet
    //populate it
    //search for stuff
    //timeContains
    public static void main(String[] args) {
        ArrayList<Double> avgList = new ArrayList<>();

        long startTime, stopTime, diff;

        for (int r = 11; r < 25; r++) { //how many averages to calculate
            ArrayList<Double> timeList = new ArrayList<Double>(); //reset the list
            BinarySearchSet<Integer> testSet = new BinarySearchSet<Integer>(); //create a binary search set
            startTime = System.nanoTime();
            while (System.nanoTime() - startTime < 1000000000) {
                //empty block
                //Wait for thread to stabalize
            }
            double N = Math.pow(2, r); //this will be the size of the collection

            for (int i = 0; i < N; i++) { //populate Test Set
                testSet.add(i); //adds I to binarySearchSet, contains 0 - 9999
            }
            System.out.println("Size of N: " + N);
            double timesToLoop = 5000000;
            startTime = System.nanoTime(); //time it
//            long timeToLoop = 5000;
            //now need to time it average of 5000
            for (int k = 0; k < timesToLoop; k++) {  //change 5000, how many times you are running the test
               //search through array for random num
                int searchInt = (int) (Math.random() * N);
                testSet.contains(searchInt); //test contains
            }
            double midPoint = System.nanoTime();

            for(int empty = 0; empty < timesToLoop; empty++){
                //runs and does nothing
            }

            stopTime = System.nanoTime();
            diff = stopTime - startTime;
            timeList.add((double) diff);
            double newAvg = ((midPoint - startTime) - (stopTime - midPoint))/ timesToLoop;
            avgList.add(newAvg);

            //TODO double averageTime = ((midpointTime - startTime) - (stopTime - midpointTime)) / timesToLoop;
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


