package assignment04;

import java.util.Comparator;

// https://big-o.io/examples/insertion-sort/java-generic/
public class InsertionSort<T extends Comparable<? super T>> {


    public static void main(String[] args) {
        String[] stringArray = {"David", "Clark", "Luke", "Kirk", "Bennit", "Mario", "Nick", "Peter", "Kristin",
                "Tori", "Rachel", "Maggie", "Anna", "Madi", "Mark"};

        //unsorted
        System.out.println("Unsorted string array ");
        System.out.println();
        for (String s : stringArray) {
            System.out.println(s);
        }

        //testing on a string array
        System.out.println();
        System.out.println("Sorted String array");
        System.out.println();
        InsertionSort<String> stringInsertionSort = new InsertionSort<>();
        stringInsertionSort.insertionSort(stringArray, Comparator.naturalOrder());

        for (String s : stringArray) {
            System.out.println(s);
        }

        //testing on int array
        Integer[] intArray = {69, 420, 666, 720, 360, 540, 180, 900};
        System.out.println("Unsorted array ");
        System.out.println();
        for (Integer integer : intArray) {
            System.out.println(integer);
        }


        InsertionSort<Integer> intSorter = new InsertionSort<>();
        intSorter.insertionSort(intArray, Comparator.naturalOrder());
        System.out.println("Sorted array ");
        System.out.println();
        for (Integer integer : intArray) {
            System.out.println(integer);
        }
    }


    public static <T> void insertionSort(T[] arr, Comparator comparator) {
        //start at first index and iterate through the end
        for (int i = 1; i < arr.length; i++) {
            int currentIndex = i;
            //Check if current index is at least one
            //If item before the current index is greater than the item at teh current index, swap them
            //while the current index is greater than zero and the index - 1 is greater than index, swap them
            while (currentIndex > 0 && comparator.compare(arr[currentIndex - 1], arr[currentIndex]) >= 1) { //if item before current index is larger
                T temp = arr[currentIndex];
                arr[currentIndex] = arr[currentIndex - 1];
                arr[currentIndex - 1] = temp;
                currentIndex--;
            }
        }
    }


}
