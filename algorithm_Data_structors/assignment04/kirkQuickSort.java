package assignment04;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
//https://examples.javacodegeeks.com/quicksort-java-algorithm-code-example/
//https://www.geeksforgeeks.org/quick-sort/
public class kirkQuickSort<T extends Comparable<? super T>> {

    public static void main(String args[]) {

        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int rand = (int) (Math.random() * arrayList.size());
            arrayList.add(rand);
        }

        System.out.println("Non Sorted ArrayList");
        System.out.println(arrayList);

        quickSortDriverKirk(arrayList, Comparator.naturalOrder());
        System.out.println("Sorted array list");
        System.out.println(arrayList);

        ArrayList<String> quickSortStringArrayList = new ArrayList<>();
        quickSortStringArrayList.add("Kirk");
        quickSortStringArrayList.add("Braden");
        quickSortStringArrayList.add("Chex");
        quickSortStringArrayList.add("Echo");
        quickSortStringArrayList.add("Shannon");
        quickSortStringArrayList.add("Zoolander");
        quickSortStringArrayList.add("Alfred HitchCock");
        quickSortStringArrayList.add("Bobo");
        quickSortStringArrayList.add("Quin");
        quickSortStringArrayList.add("Susan");
        quickSortStringArrayList.add("Jillian");
        quickSortStringArrayList.add("Dan");
        quickSortStringArrayList.add("Daniel");


        System.out.println(quickSortStringArrayList);
        kirkQuickSort.quickSortDriverKirk(quickSortStringArrayList, Comparator.naturalOrder());
        System.out.println(quickSortStringArrayList);


    }


    static <T> void quickSortDriverKirk(ArrayList<T> arrayList, Comparator comparator) {
        T[] arr = (T[]) new Object[arrayList.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arrayList.get(i);
        }

        quickSortKirk(arr, 0, arr.length - 1, comparator);

        for (int i = 0; i < arr.length; i++) {
            arrayList.set(i, arr[i]);
        }

    }

    private static <T> int partitionKirk(T[] arr, int low, int high, Comparator comparator) {

        // random(arr, low, high); //Picking random integer
        T median = median(arr, low, high, comparator);
        T pivot = median;
        // T pivot = arr[high];

        //index for smaller element
        int i = (low - 1);
        for (int j = low; j < high; j++) {

            //if current element is smaller than pivot
            if (comparator.compare(arr[j], pivot) < 0) {
                i++;

                //swap them
                T tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;

            }
        }

        T temp1 = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp1;

        return i + 1;
    }


    private static <T> void quickSortKirk(T[] arr, int low, int high, Comparator comparator) {
        if (low < high) {

            int pi = partitionKirk(arr, low, high, comparator);

            quickSortKirk(arr, low, pi - 1, comparator); //left side
            quickSortKirk(arr, pi + 1, high, comparator); //right side

        }

    }

    public static <T> void swapT(T[] arr, int low, int high) {
        T temp1 = arr[high];
        arr[high] = arr[low];
        arr[low] = temp1;
    }

    public static <T> void random(T[] arr, int low, int high) {
        Random rand = new Random();

        int random = rand.nextInt(high - low) + low;
        T temp1 = arr[random];
        arr[random] = arr[high];
        arr[high] = temp1;
    }

    //find median
    public static <T> T median(T[] arr, int low, int high, Comparator comparator) {
        int center = (low + high) / 2; //find the center

        if (comparator.compare(low, center) > 0) { //if low greater than center, swap
            swapT(arr, low, center);
        }
        if (comparator.compare(low, high) > 0) { //if low is greater than high swap
            swapT(arr, low, high);
        }

        if (comparator.compare(center, high) > 0) { // if center is greater than high, swap
            swapT(arr, center, high);
        }

        swapT(arr, center, high); //swap the center and the high
        return arr[high]; //return high


    }





}
