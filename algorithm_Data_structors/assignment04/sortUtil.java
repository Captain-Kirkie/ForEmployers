package assignment04;
//used as reference https://big-o.io/examples/merge-sort/java-generic/
//merge Sort N Log(N)

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

//used as reference http://www.java2s.com/Tutorial/Java/0140__Collections/GenericMergeSorterwithgenericComparator.htm
public class sortUtil<T extends Comparable<? super T>> {


    public static void main(String args[]) {

        //Make a reverse order comparator for integers
        Comparator<Integer> reverseOrderIntegerComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        //comparator to sort strings in reverse order
        Comparator<String> reverseOrderStringComparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        };

        //populate with random numbers
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int rand = (int) (Math.random() * 100);
            integerArrayList.add(rand);
        }
        //sort based of natural order
//        System.out.println(integerArrayList);
//        sortUtil.mergeSort(integerArrayList, Comparator.naturalOrder());
//        System.out.println("Sorted ");
//        System.out.println(integerArrayList);


        //  sort based of natural order
//        System.out.println(integerArrayList);
//        sortUtil.mergeSort(integerArrayList, reverseOrderIntegerComparator);
//        System.out.println("Sorted Reverse order ");
//        System.out.println(integerArrayList);

        //Sorting strings
        ArrayList<String> stringArrayList = new ArrayList<String>();
//        stringArrayList.add("David");
//        stringArrayList.add("Luke");
//        stringArrayList.add("Clark");
//        stringArrayList.add("Kirk");
//        stringArrayList.add("Evie");
//        stringArrayList.add("Rachel");
//        stringArrayList.add("Anna");
//        stringArrayList.add("Maggie");
//        stringArrayList.add("Mark");
//        stringArrayList.add("Madeline");
//        stringArrayList.add("Kristin");
//        stringArrayList.add("Victoria");
//        stringArrayList.add("Peter");
//        stringArrayList.add("Bennit");
//        stringArrayList.add("Mario");
//        stringArrayList.add("Nick");

//        System.out.println();
//        sortUtil.mergeSort(stringArrayList, Comparator.naturalOrder());
//        System.out.println("Sorted Strings ");
//        System.out.println(stringArrayList);
//
//        sortUtil.mergeSort(stringArrayList, reverseOrderStringComparator);
//        System.out.println("Sorted Strings Reverse Order");
//        System.out.println(stringArrayList);
//
//
//        System.out.println("QuickSort stuff");
//        System.out.println(stringArrayList);
        //quickSortDriver(stringArrayList, Comparator.naturalOrder());
//        kirkQuickSort.quickSortDriverKirk(stringArrayList, Comparator.naturalOrder());
//        System.out.println(stringArrayList);
//
//        System.out.println();
//        System.out.println("Integer Sorting");
//        System.out.println(integerArrayList);
//        //quickSortDriver(integerArrayList, Comparator.naturalOrder());
//        kirkQuickSort.quickSortDriverKirk(integerArrayList, Comparator.naturalOrder());
//        System.out.println("Sorted list" + integerArrayList);


        ArrayList<Integer> integerArrayList1 = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            int rand = (int) (Math.random() * 10000);
            integerArrayList1.add(rand);
        }

        sortUtil.mergeSort(integerArrayList1, Comparator.naturalOrder());

        System.out.println(integerArrayList1);

    }


    /**
     * merge sort driver
     * create array and pass values through so not to allocate memory everytime the
     * alogrithm is called recursivly
     * <p>
     * could improve by getting rid of for loop copying arraylists into array
     *
     * @param arrayList
     * @param comparator
     * @param <T>
     */
    public static <T> void mergeSort(ArrayList<T> arrayList, Comparator<? super T> comparator) {
        //copy the array list over to the array
        T[] array = (T[]) new Object[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        Object[] values = new Object[array.length]; //this will be sorted array
        mergeSort(array, 0, array.length - 1, comparator, values);

        //copy back to arrayList
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.set(i, array[i]);
        }
    }

    /**
     * actual merge sort, calls itself recursivly
     * splits up into smaller and smaller arrays until it reaches base case
     * then calls merge to merge elements back together in sorted order
     *
     * @param array      array to be pass
     * @param start      leftmost section of array to be sorted
     * @param end        rightmost section of array to be sorted
     * @param comparator compares items in array
     * @param values     empty array passed through to be used in merge
     * @param <T>        type
     */
    private static <T> void mergeSort(T[] array, int start, int end, Comparator<T> comparator, Object[] values) {
        if (start == end) { //if there is only one item in the array
            return;
        }
        if (end - start <= TimingMergeSortThreshHold.THRESHOLD) {
            InsertionSort.insertionSort(array, comparator);
        } else { //do merge sort
            int mid = (start + end) / 2;
            //sort both halves
            mergeSort(array, start, mid, comparator, values); //sort left half of array
            mergeSort(array, mid + 1, end, comparator, values); //sort the right

            //merge them
            merge(array, start, mid, end, comparator, values);
        }
    }

    /**
     * merge step, merges items back into array in sorted order
     * compares values in two arrays and keeps track of indices, merges into one array depending on small value
     * cleans up remainder at the end
     *
     * @param array      values passed
     * @param start      leftmost section of array to be merged
     * @param mid        stoping point for left pointer
     * @param end        stopping point for right pointer
     * @param comparator compares items
     * @param values     empty array to be merged into
     * @param <T>        types
     */

    private static <T> void merge(T[] array, int start, int mid, int end, Comparator<T> comparator, Object[] values) {
        int size = end - start + 1;


        int leftPointer = start;
        int rightPointer = mid + 1;

        int indexCurrent = 0; //index in value

        while (leftPointer <= mid && rightPointer <= end) { //do this while either pointer is less than array size
            if (comparator.compare(array[leftPointer], array[rightPointer]) <= 0) {
                values[indexCurrent] = array[leftPointer];
                leftPointer++;
            } else {
                values[indexCurrent] = array[rightPointer];
                rightPointer++;
            }
            indexCurrent++;
        }

        //clean up remaining array values
        while (leftPointer <= mid) { //clean up remaining left array
            values[indexCurrent] = array[leftPointer];
            leftPointer++;
            indexCurrent++;
        }
        //clean up right array
        while (rightPointer <= end) {
            values[indexCurrent] = array[rightPointer];
            rightPointer++;
            indexCurrent++;
        }

        for (indexCurrent = 0; indexCurrent < size; indexCurrent++) {
            array[start + indexCurrent] = (T) values[indexCurrent];
        }
    }


    /**
     * /* This is just the basic algorithm using an integer array. I wanted to make sure it worked before I made it generic,
     * * and it does. It seems like every website on the Internet has you start with both iterators on the left side instead
     * * of the way Varun explained with one pointer starting at the beginning and another starting at the end going backward.
     * * I was really stuck on trying to do it the way Varun explained which I was eventually able to figure out by looking
     * * at his slides. I always pick the last value of the array to be the pivot which turns out to be a pretty good idea
     * * because it is always random and easy to implement.
     */

    /**
     * quickSort driver, can improve by not copying arraylists into array
     * @param arrayList
     * @param comparator
     * @param <E>
     */
    public static <E> void quickSort(ArrayList<E> arrayList, Comparator comparator) {
        E[] arr = (E[]) new Object[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            arr[i] = arrayList.get(i);
        }
        Quicksort(arr, 0, arr.length - 1, comparator);

        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.set(i, arr[i]);
        }
    }

    /**
     * recursive quicksort
     * uses insertion sort depending on threshold
     *
     * @param arr
     * @param start
     * @param end
     * @param comparator
     * @param <E>
     */
    private static <E> void Quicksort(E[] arr, int start, int end, Comparator comparator) {
        if (arr.length <= TimingMergeSortThreshHold.THRESHOLD) {
            InsertionSort.insertionSort(arr, comparator);
        } else {
            // base case
            if (start >= end) {
                return;
            }

            int pivot_index = partition(arr, start, end, comparator);
            Quicksort(arr, start, pivot_index - 1, comparator);
            Quicksort(arr, pivot_index + 1, end, comparator);
        }

    }

    /**
     * partitions elements in array and compares/swaps them accordingly
     * @param arr
     * @param start
     * @param end
     * @param comparator
     * @param <T>
     * @return
     */

  //partitions elements in the array
    private static <T> int partition(T[] arr, int start, int end, Comparator comparator) {
        // kirkQuickSort.random(arr, start, end);
        int pivot_index = median(arr, start, end, comparator);
//        int pivot_index = end;

        int L = start, R = end - 1;

        while (L <= R) {
            if (comparator.compare(arr[L], arr[pivot_index]) <= 0) { //if left array is less than pivot index arr[L] <= arr[pivot_index]
                L++;
                continue;
            }
            if (comparator.compare(arr[R], arr[pivot_index]) >= 0) { //if right is less than pivot indes arr[R] >= arr[pivot_index]
                R--;
                continue;
            }
            swap(arr, L, R);
            L++;
            R--;
        }
        // After both partitions are sorted relative to the pivot, put pivot in-between the partitions
        // by swapping its value with the last value of arr[L]
        swap(arr, L, pivot_index);
        // This is a critical step. Without it quicksort doesn't work. You have to update pivot_index
        // after you swap the values. This caused a bug in my code for a while.
        pivot_index = L;
        return pivot_index;
    }

    /**
     * used for picking a random pivot in pivot testing
     * @param arr
     * @param low
     * @param high
     * @param <T>
     */
    static <T> void randomPivot(T[] arr, int low, int high) {
        Random rand = new Random();

        int pivot = rand.nextInt(high - low) + low;
        T temp1 = arr[pivot];
        arr[pivot] = arr[high];
        arr[high] = temp1;
    }

    //swap
    public static <T> void swap(T[] arr, int l, int r) {
        T temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
    }

    //prints array
    public static void printArray(int[] arr) {
        for (int number : arr) {
            System.out.print(number + ", ");
        }
        System.out.println();
    }


    /**
     * finds median in a list, used to pick the median pivot during quicksort
     *
     * @param arr
     * @param low
     * @param high
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T> int median(T[] arr, int low, int high, Comparator comparator) {
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
        return high; //return high


    }

    /**
     * swaps specified items in a list
     *
     * @param arr
     * @param low
     * @param high
     * @param <T>
     */
    public static <T> void swapT(T[] arr, int low, int high) {
        T temp1 = arr[high];
        arr[high] = arr[low];
        arr[low] = temp1;
    }

}
