//package assignment03;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.Assert.assertEquals;
//
//
//public class BinarySearchTest {
//
//
//    // int binarySearchTest = binarySearch(testArr1,60, 5,0 );
//    int[] emptyArray = new int[]{};
//    int emptyArrayTest = binarySearch(emptyArray, 32, 0, 0);
//
//    public void setTestArr1(int[] testArr1) {
//        this.testArr1 = testArr1;
//    }
//
//    @BeforeEach
//    void setUp() {
//
//    }
//
//    int[] testArr1 = new int[]{10, 20, 30, 40, 50, 60, 70};
//    int[] testArr2 = new int[]{10};
//    @Test
//    void binarySearchTest() {
//        assertEquals(2, binarySearch(testArr1, 30, 5, 0));
//        assertEquals(5, binarySearch(testArr1, 60, 5, 0)); //this failed
//        assertEquals(0, binarySearch(testArr1, 10, 5, 0));
//        assertEquals(-1, binarySearch(emptyArray, 39, 0, 0));
//        assertEquals(2, binarySearch(testArr1, 35, 5, 0));
//        assertEquals(5, binarySearch(testArr1, 65, 6,0));
//        assertEquals(6, binarySearch(testArr1, 80, 6, 0));
//        assertEquals(-1, binarySearch(testArr1, 5, 6, 0));
//        assertEquals(1, binarySearch(testArr1, 25, 6, 0));
//        assertEquals(6, binarySearch(testArr1, 75, 6, 0));
//    }
//
//    public int binarySearch(int[] array, int goal, int high, int low) {
//        int mid = -1;
//        //System.out.println("Goal: " + goal +" low: "+low+" high: "+high);
//        if(high == 0){
//            return -1;
//        }
//        if(high >= low){
//            mid = low + (high - low) / 2;
//            if(array[mid] == goal) {
//                System.out.println("mid: " + mid);
//                return mid;
//            }
//            else if(goal < array[mid]){ //search left side of array
//                return binarySearch(array, goal, mid -1, low);
//            }else{
//                return binarySearch(array, goal, high, mid + 1);
//            }
//        }
//        return high;
//    }
//
//
//}
