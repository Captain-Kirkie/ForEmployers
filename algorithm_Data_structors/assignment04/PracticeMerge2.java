package assignment04;
//TODO: there is an issue with this
//https://examples.javacodegeeks.com/merge-sort-java-algorithm-code-example/
public class PracticeMerge2 {
//if odd num of elements, > elements on left, and < on right
    static int[] originalArray = {3,5,10,45,34,77};

    public static void main(String[] args){
        int [] tmp = new int[originalArray.length];
        mergeSort(originalArray, 0,originalArray.length - 1);
        for(int element : originalArray){
            System.out.println("value " + element);
        }
    }

    static void mergeSort(int[] array, int lowIndex, int highIndex){
        if(lowIndex == highIndex){
            return;
        }
        int mid = (lowIndex + highIndex) / 2;

        mergeSort(array, lowIndex, mid); //sort left side of array
        mergeSort(array, mid + 1, highIndex); //sort right side of array

        merge(array, lowIndex, mid + 1, highIndex);

    }

    private static void merge(int [] array, int lowIndexPointer, int higherIndex, int upperIndex){
        int tempIndex = 0;
        int lowerIndex = lowIndexPointer;
        int midIndex = higherIndex - 1;
        int totalItems = upperIndex - lowerIndex + 1;

        while(lowerIndex <= midIndex && higherIndex <= upperIndex){
            if(originalArray[lowerIndex] < originalArray[higherIndex]){
                array[tempIndex++] = originalArray[lowerIndex++];
            }else{
                array[tempIndex++] = originalArray[higherIndex++];
            }
        }

        while(lowerIndex <= midIndex){
            array[tempIndex++] = originalArray[lowerIndex++];
        }
        while(higherIndex <= upperIndex){
            array[tempIndex++] = originalArray[higherIndex++];
        }
        for(int i = 0; i < totalItems; i++){
            originalArray[lowIndexPointer + i] = array[i];
        }
    }




}
