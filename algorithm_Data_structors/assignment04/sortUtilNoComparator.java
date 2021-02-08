package assignment04;

import java.util.ArrayList;
import java.util.Comparator;

public class sortUtilNoComparator <T extends Comparable<? super T>> {
    public static <T> void mergeSortDriver(ArrayList<T> arrayList, Comparator<? super T> comparator) { //takes an array and a comparator

    }
    //TODO: pass in your own comparable

    public static void main(String args[]) {
        Integer[] intArr = {5, 4, 3, 2, 7, 8, 200,459,1,99,48};
        for (int i = 0; i < intArr.length; i++) {
            System.out.println(intArr[i]);
        }

        //sort it using integers
        System.out.println("Integer sort ");
        sortUtilNoComparator<Integer> intSorter = new sortUtilNoComparator<>();
        intSorter.mergeSort(intArr, 0, intArr.length - 1);
        System.out.println();
        System.out.println("Sorted array ");

        for(int i = 0; i < intArr.length; i++){
            System.out.println(intArr[i]);
        }

        //print out unsorted string array
        String [] stringArray = {"Kirk", "Shannon", "Zach", "David", "Kyle", "Sara", "Bob"};
        System.out.println();
        System.out.println("Unsorted String array");
        for(int i = 0; i < stringArray.length; i++){
            System.out.println(stringArray[i]);
        }

        sortUtilNoComparator<String> stringSorter = new sortUtilNoComparator<>();
        stringSorter.mergeSort(stringArray, 0, stringArray.length - 1);

        System.out.println();
        System.out.println("Sorted string array ");

        for(int i = 0; i < stringArray.length; i++){
            System.out.println(stringArray[i]);
        }
    }


    void mergeSort(T[] array, int start, int end) {
        //TODO: make this into array list, then copy array list
        //base case
        if (start < end) { //when we are down to one item in array

            //find the middle
            int middle = (start + end)/ 2;


            mergeSort(array, start, middle); //sort left side
            mergeSort(array, middle + 1, end); //sort the right side

            //merge the sorted halves
            merge(array, start, middle, end);

        }
    }

    void merge(T[] array, int start, int middle, int end) {

        T[] leftArray = (T[]) new Comparable[middle - start + 1];  //size of left array
        T[] rightArray = (T[]) new Comparable[end - middle]; //size of right array

        //fill in left array
        for (int i = 0; i < leftArray.length; i++) {
            leftArray[i] = array[start + i];
        }

        //fill in the right array
        for (int i = 0; i < rightArray.length; i++) {
            rightArray[i] = array[middle + i + 1];
        }

        //intial indexes, important to update these as you move along the arrays
        int leftIndex = 0, rightIndex = 0;

        //where we start when adding sub arrays back into current array
        int currentIndex = start;

        //if either pointer gets to the end off their respective array
        while (leftIndex < leftArray.length && rightIndex < rightArray.length) {

            //if object at index left index is less than right array at right index, update array
            if (leftArray[leftIndex].compareTo(rightArray[rightIndex]) <= 0) {
                array[currentIndex] = leftArray[leftIndex];
                leftIndex++; //really important to move the cursor
            } else {
                array[currentIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            currentIndex++; //update current index
        }

        //copy the remaining elements from left array into current array
        while (leftIndex < leftArray.length)
            array[currentIndex++] = leftArray[leftIndex++];

        //copy remaining elements from left array into current array
        while (rightIndex < rightArray.length)
            array[currentIndex++] = rightArray[rightIndex++];
    }

}
