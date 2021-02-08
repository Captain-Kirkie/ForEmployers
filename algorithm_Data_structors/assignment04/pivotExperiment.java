package assignment04;

import java.util.ArrayList;
import java.util.Comparator;

public class pivotExperiment {

    public static void main(String[] args) {

        ArrayList<Double> avgList = new ArrayList<>();
        ArrayList<Integer> threshHoldList = new ArrayList<>();
        ArrayList<Integer> NList = new ArrayList<>();

        long startTime, stopTime;

        for (int i = 5; i <= 15; i++) {

            startTime = System.nanoTime();
            while (System.nanoTime() - startTime < 1000000000) {
                //empty block
                //Wait for thread to stabalize
            }
            int N = (int) Math.pow(2, i); //this will be the size of the collection
            NList.add(N);


            //System.out.println("Size of N: " + N);
            double timesToLoop = 1000;
            startTime = System.nanoTime(); //time it

            ArrayList<Integer> testArray1 = TimingMergeSortThreshHold.shuffleListNonRandom(N); //create random array of size N

            for (int k = 0; k < timesToLoop; k++) {
                ArrayList<Integer> testArray2 = new ArrayList<>(testArray1);
                kirkQuickSort.quickSortDriverKirk(testArray2, Comparator.naturalOrder());
            }

            double midPoint = System.nanoTime();

            for (int empty = 0; empty < timesToLoop; empty++) { //this accounts for the time taken to allocate memory
                ArrayList<Integer> testArray2 = new ArrayList<>(testArray1);
            }

            stopTime = System.nanoTime();

            double newAvg = (((midPoint - startTime)) - (stopTime - midPoint)) / timesToLoop;
            // System.out.println(newAvg);

            //System.out.println(newAvg);
            avgList.add(newAvg);
        }


        System.out.println("Averages ");
        for (Double d : avgList) {
            System.out.println(d);
        }

        System.out.println(NList);
        System.out.println("Thresholds");
        for (Integer i : threshHoldList) {
            System.out.println(i);
        }
    }
}
