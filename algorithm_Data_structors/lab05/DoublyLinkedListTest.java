package lab05;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoublyLinkedListTest {
    DoublyLinkedList<Integer> testList1Int, testListInt2, intListSizeOf1, emptyList;
    DoublyLinkedList<String> testList1String;


    @BeforeEach
    void setUp() {
        testList1Int = new DoublyLinkedList<Integer>();
        testList1String = new DoublyLinkedList<String>();
        testListInt2 = new DoublyLinkedList<Integer>();
        intListSizeOf1 = new DoublyLinkedList<Integer>();
        emptyList = new DoublyLinkedList<Integer>();

        //populate list before each test
        testListInt2.add(0, 10);
        testListInt2.add(1, 20);
        testListInt2.add(2, 30);
        testListInt2.add(3, 40);
        testListInt2.add(4, 50);
        testListInt2.add(5, 60);

        //populate int list
        intListSizeOf1.addFirst(50);


        testList1String.add(0, "Kirk");
        testList1String.add(0, " Is ");
        testList1String.add(0, " A ");
        testList1String.add(0, " Handsome ");
        testList1String.add(0, " Fellow ");

    }

    @AfterEach
    void reset() {
        testList1Int = null;
        testListInt2 = null;
    }


    @Test
    void addFirst() {
        testList1Int.addFirst(666);
        testList1Int.addFirst(69);
        testList1Int.addFirst(420);
        assertEquals(3, testList1Int.getSize());
        testList1Int.addFirst(51);
        testList1Int.addFirst(58);
        testList1Int.addFirst(4);
        Iterator<Integer> it = testList1Int.iterator();

    }

    @Test
    void addLast() {
        testList1Int.addLast(69);
        // System.out.println("Tail 69 " + testList1Int.getTail().getData());
        testList1Int.addLast(420);
        //System.out.println("new Tail 420 " + testList1Int.getTail().getData());
        //System.out.println("Head 69 " + testList1Int.getHead().getData());

        // assertEquals(2, testList1Int.getSize());
    }

    @Test
    void add() {
        testList1Int.addFirst(10);
        testList1Int.addFirst(20);
        testList1Int.addFirst(30);
        testList1Int.addFirst(40);
        testList1Int.addFirst(50);
        testList1Int.addFirst(60);
        testList1Int.addFirst(70);
        testList1Int.addFirst(80);
        testList1Int.add(2, 5);
        testList1Int.add(8, 5);

//        System.out.println("Head: " + testList1Int.getHead().getData());
//        System.out.println("Tail: " + testList1Int.getTail().getData());
//        Iterator<Integer> it = testList1Int.iterator();
//
//        while (it.hasNext()) {
//            System.out.println("This itterator is working " + it.next());
//        }
    }

    //10,20,30,40,50
    @Test
    void getFirst() {
        assertEquals(10, testListInt2.getFirst());

    }

    @Test
    void getLast() {
        //assertEquals(50, testListInt2.getLast());
    }


    //10,20,30,40,50
    @Test
    void get() {
        assertEquals(20, testListInt2.get(1));
        assertEquals(50, testListInt2.get(4));
        assertEquals(10, testListInt2.get(0));
        assertEquals(50, testListInt2.get(4));
    }

    //10,20,30,40,50
    @Test
    void removeFirst() {
        assertEquals(10, testListInt2.removeFirst());
        assertEquals(20, testListInt2.removeFirst());
        assertEquals(30, testListInt2.removeFirst());
        assertEquals(40, testListInt2.removeFirst());
        assertEquals(50, testListInt2.removeFirst());
        assertEquals(60, testListInt2.removeFirst());
        assertEquals(null, testListInt2.removeFirst());

    }

    @Test
    void removeLast() {
        assertEquals(60, testListInt2.removeLast());
        assertEquals(50, testListInt2.removeLast());
        assertEquals(40, testListInt2.removeLast());
        assertEquals(30, testListInt2.removeLast());
        assertEquals(20, testListInt2.removeLast());
        assertEquals(10, testListInt2.removeLast());
        testList1String.remove(4);
        // testList1String.addLast("Hello Beautiful");

        Object[] arrString = testList1String.toArray();
//        for(Object obj: arrString){
//            System.out.println(obj);
//        }
    }

    @Test
    void remove() {
        //10,20,30,40,50, 60
        //remove 30
        assertEquals(30, testListInt2.remove(2));
        assertEquals(10, testListInt2.remove(0));
        assertEquals(60, testListInt2.remove(3));
        // testListInt2.remove(-1);
        Iterator<Integer> it = testListInt2.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }
    }

    //10,20,30,40,50,60
    // 0  1  2  3  4   5
    @Test
    void indexOf() {
        testListInt2.indexOf(20);
        testListInt2.indexOf(10);
        assertEquals(1, testListInt2.indexOf(20));
        assertEquals(0, testListInt2.indexOf(10));
        assertEquals(2, testListInt2.indexOf(30));
        assertEquals(5, testListInt2.indexOf(60));
        assertEquals(4, testList1String.indexOf("Kirk"));
    }

    @Test
    void lastIndexOf() {
        assertEquals(5, testListInt2.lastIndexOf(60));
        assertEquals(4, testListInt2.lastIndexOf(50));
        testListInt2.addLast(50);
        assertEquals(6, testListInt2.lastIndexOf(50));

    }

    @Test
    void size() {
        assertEquals(6, testListInt2.size());
    }

    @Test
    void isEmpty() {
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void clear() {
        Iterator<Integer> it = testListInt2.iterator();
        testListInt2.clear();
        // assertFalse(it.hasNext());

    }

    @Test
    void toArray() {
        Object[] arr = testListInt2.toArray();
//        for(Object obj : arr){
////            System.out.println("Object array blah blah blah " + obj);
////            System.out.println(obj);
//        }

        Object[] arrString = testList1String.toArray();
//        for(Object obj: arrString){
////            System.out.println(obj);
//        }

    }

    @Test
    void hasNext() {
    }

    @Test
    void next() {
//        Iterator<Integer> it = testListInt2.iterator();
//        System.out.println("Iterator test ");
//        for(int i = 0; i < 6; i++){
//            System.out.println(it.next());
//        }
    }

    @Test
        //for iterator //10,20,30,40,50,60
    void testRemove() {
        System.out.println("Remove test");
        Iterator<Integer> it = testListInt2.iterator();
        System.out.println("For each loop");

            while(it.hasNext()){
               if(it.next() == 20)
                   it.remove();
                System.out.println("hello");
            }
            assertEquals(5, testList1String.size());






    }
}