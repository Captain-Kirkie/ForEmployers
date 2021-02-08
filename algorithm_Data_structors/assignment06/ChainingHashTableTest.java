package assignment06;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChainingHashTableTest {

    //Collections/ArrayLists
    ArrayList<String> arrayList1 = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();
    ArrayList<String> arrayListMinusAndy = new ArrayList<>();



    //Functors
    ReallyBadHashFunctor badHashFunctor = new ReallyBadHashFunctor();
    MediocreHashFunctor mediocreHashFunctor = new MediocreHashFunctor();
    GoodHashFunctor goodHashFunctor = new GoodHashFunctor();
    GoodHashFunctor2 goodHashFunctor2 = new GoodHashFunctor2();

    //HashTables
    ChainingHashTable badHashTest1 = new ChainingHashTable(10, badHashFunctor);
    ChainingHashTable badHashTest2 = new ChainingHashTable(50, badHashFunctor);
    ChainingHashTable mediocreHashTable1 = new ChainingHashTable(50, mediocreHashFunctor);
    ChainingHashTable goodHashTable1 = new ChainingHashTable(50, goodHashFunctor);

    ChainingHashTable emptyHashTable = new ChainingHashTable(100, badHashFunctor);



    File file = new File("src/assignment06/dictionary.txt");
    File collisionFile = new File("src/assignment06/collision.txt");
    ChainingHashTable getDictionaryHashTableGood2 = new ChainingHashTable(goodHashFunctor2, file);
    ChainingHashTable dictionaryHashTableGood = new ChainingHashTable(goodHashFunctor, file);
    ChainingHashTable dictionaryHashTableMediocre = new ChainingHashTable(mediocreHashFunctor, file);
    ChainingHashTable dictionaryHashTableBad = new ChainingHashTable(badHashFunctor, file);

    ChainingHashTable collisionHashTable = new ChainingHashTable(badHashFunctor, collisionFile);

    @BeforeEach
    void setup() {
        arrayList1.add("Bilbo Baggins");
        arrayList1.add("Frodo Baggins");
        arrayList1.add("Gandalf");
        arrayList1.add("Aragorn");
        arrayList1.add("Legolas");
        arrayList1.add("Gimli");
        arrayList1.add("Boromir");

        arrayList2.add("Leslie");
        arrayList2.add("Ben");
        arrayList2.add("Chris");
        arrayList2.add("Andy");
        arrayList2.add("April");

        arrayListMinusAndy.add("Leslie");
        arrayListMinusAndy.add("Ben");
        arrayListMinusAndy.add("Chris");
        arrayListMinusAndy.add("April");

        //Hash Tables
        badHashTest2.addAll(arrayList2); //add to second bad Array List
        goodHashTable1.addAll(arrayList2);

    }


    @AfterEach
    void reset() {
        badHashTest1 = null;
        badHashTest2 = null;
        arrayList1 = null;
        arrayList2 = null;
        arrayListMinusAndy = null;
        dictionaryHashTableBad = null;
        dictionaryHashTableMediocre = null;
        dictionaryHashTableGood = null;
    }

    @Test
    void collisionTest() {
        System.out.println(collisionHashTable.getCollision());
        System.out.println("Good functor 1 " + dictionaryHashTableGood.getCollision());
//        ChainingHashTable.printHashTable(dictionaryHashTableGood);
        System.out.println("Good functor 2 " + getDictionaryHashTableGood2.getCollision());
        System.out.println("Mediocre functor " + dictionaryHashTableMediocre.getCollision());
        System.out.println("Bad functor " + dictionaryHashTableBad.getCollision());
        System.out.println("Collision Hash Table " + collisionHashTable.getCollision());
        System.out.println("size  " + dictionaryHashTableMediocre.size());

        LinkedList<String>[] nullCountList = dictionaryHashTableGood.getStorage();
        int nullcount = 0;
        for(int i = 0; i < nullCountList.length; i++){
            if(nullCountList[i] == null){
                nullcount++;
            }
        }
        System.out.println("This is the nullcount " + nullcount);
        System.out.println();
    }

    @Test
    void constructorTest() {
        //System.out.println(getDictionaryHashTableGood2);

//        System.out.println("Good");
//        ChainingHashTable.printHashTable(dictionaryHashTableGood);
//        System.out.println("Mediocre");
//        System.out.println();
//        ChainingHashTable.printHashTable(dictionaryHashTableMediocre);
//        System.out.println();
//        System.out.println("Bad");
//        ChainingHashTable.printHashTable(dictionaryHashTableBad);
    }

    @Test
    void add() {
        assertTrue(badHashTest1.add("Kirk"));
        assertTrue(badHashTest1.add("Shannon"));
        assertTrue(badHashTest1.add("Sara"));
        assertTrue(badHashTest1.add("Varun"));
        assertFalse(badHashTest1.add("Kirk"));
        assertFalse(badHashTest1.add("Varun"));
        assertFalse(badHashTest1.add("Shannon"));
    }

    @Test
    void addAll() {
        assertTrue(badHashTest2.addAll(arrayList1));
        assertFalse(badHashTest2.addAll(arrayListMinusAndy));
        assertTrue(mediocreHashTable1.addAll(arrayList1));
    }

    @Test
    void clear() {
        assertTrue(badHashTest1.addAll(arrayList1));
        badHashTest1.clear();
        badHashTest1.clear();
        assertEquals(0, badHashTest1.size());
        dictionaryHashTableGood.clear();
        LinkedList<String>[] dictionaryLinkedList = dictionaryHashTableGood.getStorage();

        for (int i = 0; i < dictionaryLinkedList.length; i++) {
            assertEquals(null, dictionaryLinkedList[i]);
        }

    }

    @Test
    void contains() {
        assertTrue(badHashTest1.addAll(arrayList1));
        assertTrue(badHashTest1.contains("Aragorn"));
        assertTrue(badHashTest1.contains("Legolas"));
        assertTrue(badHashTest1.contains("Gimli"));
        assertFalse(badHashTest1.contains("Kirk"));
        assertFalse(badHashTest1.contains("Shannon"));
        assertFalse(dictionaryHashTableGood.contains("BillyJoe!"));
    }

    @Test
    void containsAll() {
        assertTrue(badHashTest2.containsAll(arrayList2));
        assertFalse(badHashTest2.containsAll(arrayList1));
    }

    @Test
    void isEmpty() {
        assertTrue(emptyHashTable.isEmpty());
        dictionaryHashTableGood.clear();
        assertTrue(dictionaryHashTableGood.isEmpty());
    }

    /*
    acanthuses
    kidnapping
    weeps
    thief
     */
    @Test
    void remove() {
        assertTrue(badHashTest2.remove("Andy"));
        assertTrue(dictionaryHashTableGood.contains("acanthuses"));
        assertTrue(dictionaryHashTableGood.contains("thief"));
        assertTrue(dictionaryHashTableGood.remove("thief"));
        assertTrue(dictionaryHashTableGood.remove("weeps"));
        assertTrue(dictionaryHashTableGood.remove("kidnapping"));
        assertTrue(dictionaryHashTableGood.remove("acanthuses"));
        assertFalse(dictionaryHashTableGood.contains("acanthuses"));
        assertFalse(dictionaryHashTableGood.contains("kidnapping"));
    }

    @Test
    void removeAll() {
        assertTrue(badHashTest2.removeAll(arrayListMinusAndy));
        badHashTest2.remove("Chris");
        assertTrue(badHashTest2.removeAll(arrayList2));
    }

    @Test
    void size() {
        assertEquals(0, emptyHashTable.size());
        assertEquals(5, badHashTest2.size());
        assertEquals(2914, dictionaryHashTableBad.size());
        dictionaryHashTableGood.clear();
        assertEquals(0, dictionaryHashTableGood.size());

    }


}