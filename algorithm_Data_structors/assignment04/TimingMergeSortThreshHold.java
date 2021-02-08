package assignment04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class TimingMergeSortThreshHold {
      static int THRESHOLD = 100;

    public static void main(String[] args) {

        ArrayList<Double> avgList = new ArrayList<>();
        ArrayList<Integer> threshHoldList = new ArrayList<>();

        long startTime, stopTime;


        for (int i = 0; i <= 50; i++) {

            startTime = System.nanoTime();
            while (System.nanoTime() - startTime < 1000000000) {
                //empty block
                //Wait for thread to stabalize
            }
            int N = (int) Math.pow(2, 11); //this will be the size of the collection


            //System.out.println("Size of N: " + N);
            double timesToLoop = 1000;
            startTime = System.nanoTime(); //time it
            ArrayList<Integer> testArray1 = TimingMergeSortThreshHold.genrateRandomArray(N); //create random array of size N
            //System.out.println("this is orginial " + testArray1);
            for (int k = 0; k < timesToLoop; k++) {
                ArrayList<Integer> testArray2 = new ArrayList<>(testArray1);
                sortUtil.mergeSort(testArray2, Comparator.naturalOrder());
            }

            double midPoint = System.nanoTime();

            for (int empty = 0; empty < timesToLoop; empty++) { //this accounts for the time taken to allocate memory
                ArrayList<Integer> testArray2 = new ArrayList<>(testArray1);
            }

            stopTime = System.nanoTime();
            threshHoldList.add(THRESHOLD);
            double newAvg = (((midPoint - startTime)) - (stopTime - midPoint)) / timesToLoop;
            // System.out.println(newAvg);
           THRESHOLD = THRESHOLD + 10;
            //System.out.println(newAvg);
            avgList.add(newAvg);
        }

        System.out.println("Averages ");
        for (Double d : avgList) {
            System.out.println(d);
        }

        System.out.println("Thresholds");
        for (Integer i : threshHoldList) {
            System.out.println(i);
        }

    }

    /**
     * generates an array list of specified size in sorted order
     *
     * @param Size
     * @return
     */
    //generate best case
    public static ArrayList<Integer> generateBestCase(int Size) {
        ArrayList<Integer> bestCaseDoubleArray = new ArrayList<>(); //will already be in sorted order
        for (int i = 0; i <= Size; i++) {
            bestCaseDoubleArray.add(i);
        }
        return bestCaseDoubleArray;
    }

    /**
     * Genearates an randomly Orders list of specified size
     *
     * @param size
     * @return
     */
    //average case
    public static ArrayList<Integer> genrateRandomArray(int size) {
        ArrayList<Integer> randomList = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            int rand = (int) (Math.random() * size);
            randomList.add(rand);
        }
        return randomList;
    }

    /**
     * generates a list that is sorted in reverse order
     * @param size
     * @return
     */

    //worst case
    public static ArrayList<Integer> generateWorstCase(int size) {
        ArrayList<Integer> worstCaseArrayList = new ArrayList<>();
        for (int i = size; i > 0; i--) {
            worstCaseArrayList.add(i);
        }
        return worstCaseArrayList;
    }

    /**
     * returns a shuffled array always in the same order
     * @param size
     * @return
     */
    public static ArrayList<Integer> shuffleListNonRandom(int size) {
        ArrayList<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < size; i++) { //populate a sorted list
            randomList.add(i);
        }
        Random ran = new Random(size);
        for (int i = 0; i < size; i++) {
            int nxt = ran.nextInt(size);
            Collections.swap(randomList, i, nxt);
        }
        return randomList;
    }

    /**
     * This randomly shuffles a list
     *
     * @param arrayList
     */
    public static void shuffleListRandom(ArrayList arrayList) {
        Random ran = new Random();
        for (int i = 0; i < arrayList.size(); i++) {
            int nxt = ran.nextInt(arrayList.size());
            Collections.swap(arrayList, i, nxt);
        }
    }

}
