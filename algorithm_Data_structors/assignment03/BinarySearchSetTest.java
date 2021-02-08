package assignment03;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchSetTest<E> {
    int[] intArrayTest1, intArrayTest2;
    BinarySearchSet testSet1, testSet1String, growSet1, nameSet1, negSet1,
            emptyTest1, emptyTest2, listToClear1, containAllSearchSet1,
            intSetRemoveTest, removeBinarySearchSet, emptyBinSearchSet2;
    String AAA, AAB, AAC, AAADuplicate, AAZ;
    ArrayList<Integer> testArrayList, emptyArrayList, containsAllTest1, removeArraylist1;

    BinarySearchSet<Integer> intTestSet = new BinarySearchSet<Integer>();

    //LapTops
    BinarySearchSet lapTopSearchSet1, lapTopSearchSet2ID, lapTopSearchSetEmpty;
    LapTop labTop1, labTop2, labTop3, labTop4, labTop5;

    @BeforeEach
    void setup() {
        labTop1 = new LapTop("LabTop1", 9999999, "Dell");
        labTop2 = new LapTop("LabTop2", 9999998, "Apple");
        labTop3 = new LapTop("LabTop3", 9999997, "Windows");
        labTop4 = new LapTop("LabTop4", 9999996, "Windows");
        labTop5 = new LapTop("LabTop5", 9999995, "Windows");

        //Comparator
        Comparator labTopNamecomparator = new orderLaptopByName();
        Comparator lapTopIDComparator = new orderLaptopByID();

        lapTopSearchSet2ID = new BinarySearchSet(lapTopIDComparator);
        lapTopSearchSetEmpty = new BinarySearchSet();
        emptyBinSearchSet2 = new BinarySearchSet();
        removeBinarySearchSet = new BinarySearchSet();
        removeArraylist1 = new ArrayList<>();
        intSetRemoveTest = new BinarySearchSet();
        lapTopSearchSet1 = new BinarySearchSet(labTopNamecomparator);
        testSet1 = new BinarySearchSet(); //comparator is returning null
        testSet1String = new BinarySearchSet();
        growSet1 = new BinarySearchSet();
        nameSet1 = new BinarySearchSet();
        negSet1 = new BinarySearchSet();
        emptyTest1 = new BinarySearchSet();
        emptyArrayList = new ArrayList<>();
        intArrayTest2 = new int[10];
        testArrayList = new ArrayList<>();
        emptyTest2 = new BinarySearchSet();
        listToClear1 = new BinarySearchSet();
        containsAllTest1 = new ArrayList<>();
        containAllSearchSet1 = new BinarySearchSet();
        AAA = "AAA";
        AAB = "AAB";
        AAC = "AAC";
        AAADuplicate = "AAA";
        AAZ = "AAZ";

        intTestSet.add(54);
        intTestSet.add(300);
        intTestSet.add(-45);
        intTestSet.add(-5);
        intTestSet.add(800);
        intTestSet.add(500);
        intTestSet.add(600);
        intTestSet.add(900);


        nameSet1.add("Zoey DeShanelle");
        nameSet1.add("AAron Rogers");
        nameSet1.add("Queen Latifa");
        nameSet1.add("Alex Trabek");
        nameSet1.add("Hansel is so hot right now");

        for (int i = -100; i > 0; i++) { //populate set with neg numbers
            negSet1.add(i);
        }

        for (int i = 0; i < 300; i++) { //populate contains all arraylist and bin search set
            containsAllTest1.add(i);
            containAllSearchSet1.add(i);
        }

        for (int i = 0; i < 100; i++) {
            listToClear1.add(i);
        }

        //populate for remove test
        for (int i = 0; i < 20; i++) {
            intSetRemoveTest.add(i);
        }

        //populate Test array list
        int count = 0;
        for (int i = 0; i < 10; i++) {
            count--;
            testArrayList.add(i, count);
        }
    }

    @AfterEach
    void reset() {
        lapTopSearchSet1 = null;
        lapTopSearchSet2ID = null;
        lapTopSearchSetEmpty = null;
    }

    @Test
    void lapTopAddTest() { //TODO: Come back to this
        assertTrue(lapTopSearchSet1.add(labTop1));
        assertTrue(lapTopSearchSet1.add(labTop2));
        assertTrue(lapTopSearchSet1.add(labTop3));
        assertTrue(lapTopSearchSet1.add(labTop4));
        assertTrue(lapTopSearchSet1.add(labTop5));
        assertFalse(lapTopSearchSet1.add(labTop1));
        assertFalse(lapTopSearchSet1.add(labTop2));
        assertFalse(lapTopSearchSet1.add(labTop3));

        for (Object obj : lapTopSearchSet1) {
            LapTop lap = (LapTop) obj;
            System.out.println(lap.name);
        }
    }
    @Test
    void lapTopCompareIDTest() {
        System.out.println("Laptop ID comparions test");
        assertTrue(lapTopSearchSet2ID.add(labTop1));
        assertTrue(lapTopSearchSet2ID.add(labTop2));
        assertTrue(lapTopSearchSet2ID.add(labTop3));
        assertTrue(lapTopSearchSet2ID.add(labTop4));
        assertTrue(lapTopSearchSet2ID.add(labTop5));

        for (Object obj : lapTopSearchSet2ID) {
            LapTop lap = (LapTop) obj;
            System.out.println(lap.name);
        }


    }

    @Test
    void lapTopfirstAndLast() {
        lapTopSearchSet1.add(labTop1);
        lapTopSearchSet1.add(labTop2);
        lapTopSearchSet1.add(labTop3);
        lapTopSearchSet1.add(labTop4);
        lapTopSearchSet1.add(labTop5);
        assertEquals(labTop1, lapTopSearchSet1.first());
        assertEquals(labTop5, lapTopSearchSet1.last());
    }

    @Test
    void lapTopContains(){
        lapTopSearchSet1.add(labTop1);
        lapTopSearchSet1.add(labTop2);
        lapTopSearchSet1.add(labTop3);
        lapTopSearchSet1.add(labTop4);
        lapTopSearchSet1.add(labTop5);

        assertTrue(lapTopSearchSet1.contains(labTop1));
        assertTrue(lapTopSearchSet1.contains(labTop5));


    }

    @Test
    void removeLaptop(){
        lapTopSearchSet1.add(labTop1);
        lapTopSearchSet1.add(labTop2);
        lapTopSearchSet1.add(labTop3);
        lapTopSearchSet1.add(labTop4);
        lapTopSearchSet1.add(labTop5);

        lapTopSearchSet1.remove(labTop4);
        assertFalse(lapTopSearchSet1.contains(labTop4));


    }


    @Test
    void intAddTest() {
        //10, 30, -1
        assertEquals(true, testSet1.add(10));
        assertEquals(true, testSet1.add(30));
        assertEquals(false, testSet1.add(30));
        assertEquals(true, testSet1.add(-1));
        assertEquals(false, testSet1.add(-1));
    }

    @Test
    void growTest() {
        assertEquals(10, growSet1.getCapacity());
        for (int i = 0; i < 10; i++) { //grow size to 20
            growSet1.add(i);
        }
        assertEquals(20, growSet1.getCapacity());
        for (int i = 10; i < 20; i++) { //grow size to 20
            growSet1.add(i);
        }
        assertEquals(40, growSet1.getCapacity());
    }

    @Test
    void stringAddTest() {
        assertTrue(testSet1String.add(AAA));
        assertTrue(testSet1String.add(AAC));
        assertTrue(testSet1String.add(AAZ));//add to end of the array
        assertTrue(testSet1String.add(AAB));
        assertEquals(false, testSet1String.add(AAADuplicate));
    }

    @Test
    void binarySearch() {
        //tested elseWhere
    }

    @Test
    void comparator() {

        //TODO: Figure out how to test
    }

    @Test
    void first() {
        assertEquals(-45, intTestSet.first());
        assertEquals("AAron Rogers", nameSet1.first());
    }

    @Test
    void last() {
        //intTestSet.add(900);
        assertEquals("Zoey DeShanelle", nameSet1.last());
        assertEquals(900, intTestSet.last());
    }

    @Test
    void addAll() {
        emptyTest2.addAll(testArrayList);
    }

    @Test
    void clear() {
        listToClear1.clear();
        Object[] listToClearArray = listToClear1.toArray();
        for (Object obj : listToClearArray) {
            //System.out.println("NULL OBJ " + obj);
            assertTrue(obj == null);
        }
    }

    @Test
    void contains() {
        assertEquals(true, intTestSet.contains(800));
        assertTrue(nameSet1.contains("Zoey DeShanelle"));
        assertEquals(true, intTestSet.contains(54));
    }

    @Test
    void containsAll() {
        assertEquals(true, containAllSearchSet1.containsAll(containsAllTest1));
        assertEquals(false, containAllSearchSet1.containsAll(testArrayList));
    }

    @Test
    void isEmpty() {
        assertEquals(true, emptyTest1.isEmpty());
        assertEquals(false, containAllSearchSet1.isEmpty());
    }

    @Test
    void iterator() {
    }

    @Test
    void remove() {
        intSetRemoveTest.remove(15);
        Object[] testArray = intSetRemoveTest.toArray();
//        for (int i = 0; i < testArray.length; i++) {
//            System.out.println(testArray[i]);
//        }
        assertEquals(false, intSetRemoveTest.contains(15));
        intSetRemoveTest.remove(10);
        assertEquals(false, intSetRemoveTest.contains(10));
        assertEquals(false, intSetRemoveTest.remove(100));

    }

    //
    @Test
    void size() {
        BinarySearchSet sizeTest = new BinarySearchSet();
        for (int i = 0; i < 100; i++) {
            sizeTest.add(i);
        }
        int sizeTmp = sizeTest.size();
        assertEquals(100, sizeTmp);
    }

    @Test
    void toArray() {
        Object[] newArrInt = intTestSet.toArray();
//        for (Object obj : newArrInt) { //print new Array
//            System.out.println(obj);
//        }
        Object[] newArrayString = nameSet1.toArray();
//        for (Object obj : newArrayString) {
//            System.out.println(obj);
//        }
    }


    @Test
    void removeAll() { //populate an binarySearchSet with pos and neg numbers, remove only pos
        for (int i = 0; i < 100; i++) {
            removeArraylist1.add(i); //populate arraylist with positive numbers
        }
        removeBinarySearchSet.addAll(removeArraylist1);
        Object[] posNumArray = removeBinarySearchSet.toArray();
        int neg = -1;
        for (int i = 0; i < 50; i++) { //also add neg numbers to the array
            removeBinarySearchSet.add(neg);
            neg--; //add a bunch of neg number to binarySearchSet
        }
        removeBinarySearchSet.removeAll(removeArraylist1);
        for (int i = 0; i < posNumArray.length; i++) {
            assertEquals(false, removeBinarySearchSet.contains(posNumArray[i]));
        }

    }

    protected class orderLaptopByID implements Comparator<LapTop> {
        @Override
        public int compare(LapTop o1, LapTop o2) {
            if (o1.getID() < o2.getID()) {
                return -1;
            } else if (o1.getID() > o2.getID()) {
                return 1;
            } else {
                return 0;
            }
//            return o1.getID().compareTo(o2.getID());
        }
    }

    //comparator stuff
    protected class orderLaptopByName implements Comparator<LapTop> {
        @Override
        public int compare(LapTop o1, LapTop o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}