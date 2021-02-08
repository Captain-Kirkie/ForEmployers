package assignment05.Testing;

import assignment05.BinarySearchTree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
//dot -Tpng Tree.dot -o Tree.png
//TODO:Testing
//what if searching for a root?
//test find successor
//could do something ittertively if reach base case

class BinarySearchTreeTest {
    BinarySearchTree<String> stringTree1 = new BinarySearchTree<>();
    BinarySearchTree<String> stringTree3 = new BinarySearchTree<>();
    BinarySearchTree<String> stringTree2 = new BinarySearchTree<>();
    BinarySearchTree<String> removeTree = new BinarySearchTree<>();
    BinarySearchTree<Integer> intTree = new BinarySearchTree<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    BinarySearchTree<Integer> intTree2 = new BinarySearchTree<>();
    BinarySearchTree<String> lordOfRingsList = new BinarySearchTree<>();
    ArrayList<String> lordArray = new ArrayList<>();
    BinarySearchTree<Integer> sizeTree = new BinarySearchTree<>();
    ArrayList<String> lordArray2 = new ArrayList<>();
    BinarySearchTree<Integer> jeffTest = new BinarySearchTree<>();
    BinarySearchTree<Integer> jeffTestLeft = new BinarySearchTree<>();
    BinarySearchTree<String> helloWorldTree = new BinarySearchTree<>();
    ArrayList<String> helloWorldArrayList = new ArrayList<>();
    ArrayList<String> parksAndRecArrayList = new ArrayList<>();
    BinarySearchTree<Integer> integerTestTreeSmall = new BinarySearchTree<>();
    ArrayList<Integer> smallArrList = new ArrayList<>();
    ArrayList<Integer> intSmallArrayListContainsAll = new ArrayList<>();
    ArrayList<Integer> intSmallArrayListContainsAll2 = new ArrayList<>();
    BinarySearchTree<Integer> emptyTree = new BinarySearchTree<>();
    ArrayList<Integer> removeAllIntArrayList = new ArrayList<>();
    BinarySearchTree<Integer> treeOfOne = new BinarySearchTree<>();


    @BeforeEach
    void setUp() {
        stringArrayList.add("kirk");
        stringArrayList.add("z");
        stringArrayList.add("Alfred");
        stringArrayList.add("Zoolander");
        stringArrayList.add("shannon");
        stringArrayList.add("Echo");
        stringArrayList.add("Chex");
        stringArrayList.add("Pippen");
        stringArrayList.add("Ollie");
        stringArrayList.add("Mary");
        stringArrayList.add("Sauron");
        stringArrayList.add("Borameir");
        stringArrayList.add("Elron");
        stringArrayList.add("Aragorn");


        stringTree2.add("Bilbo");
        stringTree2.add("Frodo");
        stringTree2.add("Gandalf");
        stringTree2.add("Gimli");
        stringTree2.add("Aragorn");
        stringTree2.add("Legless");
        stringTree2.add("Sam");
        stringTree2.add("Zoey");
        stringTree2.add("Sara");
        stringTree2.add("kirk");
        stringTree2.add("Justin");
        stringTree2.add("Karen");
        stringTree2.add("elron");

        removeTree.add("A");
        removeTree.add("B");
        removeTree.add("C");
        removeTree.add("D");
        removeTree.add("E");
        removeTree.add("F");
        removeTree.add("G");

        removeTree.add("Kirk");
        removeTree.add("Shannon");
        removeTree.add("Bob");
        removeTree.add("Kyle");

        lordOfRingsList.add("Aragorn");
        lordOfRingsList.add("Legolas");
        lordOfRingsList.add("Gollum");
        lordOfRingsList.add("Gandalf");
        lordOfRingsList.add("Frodo");
        lordOfRingsList.add("Boromir");
        lordOfRingsList.add("Gimili");
        lordOfRingsList.add("Sam");
        lordOfRingsList.add("Mary");
        lordOfRingsList.add("Pippin");

        lordArray.add("Aragorn");
        lordArray.add("Gollum");
        lordArray.add("Frodo");
        lordArray.add("sam");

        lordArray2.add("BobRoss");
        lordArray2.add("Obama");
        lordArray2.add("Kyle");

        jeffTest.add(1);
        jeffTest.add(2);
        jeffTest.add(3);
        jeffTest.add(4);
        jeffTest.add(5);
        jeffTest.writeDot("JefTestBefore.dot");

        jeffTestLeft.add(5);
        jeffTestLeft.add(4);
        jeffTestLeft.add(3);
        jeffTestLeft.add(2);
        jeffTestLeft.add(1);
        jeffTestLeft.writeDot("leftSide.dot");

        helloWorldTree.add("hello");
        helloWorldTree.add("cruel");
        helloWorldTree.add("world");

        helloWorldArrayList.add("hello");
        helloWorldArrayList.add("cruel");
        helloWorldArrayList.add("world");

        parksAndRecArrayList.add("April");
        parksAndRecArrayList.add("Andy");
        parksAndRecArrayList.add("Ron");
        parksAndRecArrayList.add("Leslie");
        parksAndRecArrayList.add("Jerry");
        parksAndRecArrayList.add("Ben");
        parksAndRecArrayList.add("Jean-Ralphio");

        integerTestTreeSmall.add(5);
        integerTestTreeSmall.add(10);
        integerTestTreeSmall.add(2);
        integerTestTreeSmall.add(7);
        integerTestTreeSmall.add(6);
        integerTestTreeSmall.add(4);
        integerTestTreeSmall.add(1);
        integerTestTreeSmall.add(11);
        integerTestTreeSmall.add(8);
        integerTestTreeSmall.writeDot("SmallIntTree.dot");

        intSmallArrayListContainsAll.add(10);
        intSmallArrayListContainsAll.add(11);
        intSmallArrayListContainsAll.add(6);
        intSmallArrayListContainsAll.add(8);
        intSmallArrayListContainsAll.add(8);
        intSmallArrayListContainsAll.add(5);

        intSmallArrayListContainsAll2.add(10);
        intSmallArrayListContainsAll2.add(11);
        intSmallArrayListContainsAll2.add(6);
        intSmallArrayListContainsAll2.add(8);
        intSmallArrayListContainsAll2.add(8);
        intSmallArrayListContainsAll2.add(5);
        intSmallArrayListContainsAll2.add(40);

        treeOfOne.add(50);


        for (int i = 20; i <= 30; i++) { //populate small int test array
            int r = i;
            if (r % 2 == 0)
                r = r * (-1);
            smallArrList.add(i);
        }


        for (int i = 0; i < 50; i++) {
            int rand = (int) (Math.random() * 500);
            intTree.add(rand);
        }

        //sets up random num between 0 and 100
        for (int i = 0; i < 15; i++) {
            int rand = (int) (Math.random() * 100);
            intTree2.add(rand);
        }

        for (int i = 0; i < 100; i++) {
            removeAllIntArrayList.add(i);
        }
    }

    @AfterEach
    void reset() {
        stringTree1 = null;
        stringArrayList = null;
        stringTree2 = null;
        lordOfRingsList = null;
        integerTestTreeSmall = null;
        emptyTree = null;


    }


    @Test
    void add() {
        assertTrue(stringTree1.add("kirk"));
        assertFalse(stringTree1.add("kirk"));
        assertFalse(stringTree1.add("Kirk"));
        assertTrue(stringTree1.add("z"));
        assertTrue(stringTree1.add("Alfred"));
        assertTrue(stringTree1.add("Zoolander"));
        assertTrue(stringTree1.add("shannon"));
        assertFalse(stringTree1.add("Shannon"));


        assertFalse(lordOfRingsList.add("Gandalf"));
        assertFalse(lordOfRingsList.add("Gimili"));
        assertTrue(lordOfRingsList.add("Sauron"));
        //lordOfRingsList.writeDot("LordOfRingsFile.dot");
        assertTrue(integerTestTreeSmall.add(100)); //add to farthest right
        assertTrue((integerTestTreeSmall.add(-100))); //add to the farthest left


    }
    //aragorn, gollum, frodo, sam

    @Test
    void addAll() {
        assertTrue(stringTree1.addAll(stringArrayList));
        lordOfRingsList.addAll(lordArray2);
        assertTrue(lordOfRingsList.contains("obama"));
        assertTrue(lordOfRingsList.contains("Obama"));
        //System.out.println("array");
        // System.out.println(lordOfRingsList.toArrayList());

        integerTestTreeSmall.addAll(smallArrList);
        for (int s : smallArrList) {
            assertTrue(integerTestTreeSmall.contains(s));
        }


    }

    @Test
    void clear() {
        stringTree1.add("kirk");
        stringTree1.add("z");
        stringTree1.add("Alfred");
        stringTree1.add("Indumathi");
        stringTree1.add("Zoolander");
        stringTree1.add("Bob");
        stringTree1.add("Shannon");
        stringTree1.add("shannon");
        stringTree1.add("AB");
        stringTree1.add("a");
        stringTree1.writeDot("beforeClear.dot");
        stringTree1.clear();
        stringTree1.writeDot("afterClear.dot");
        assertTrue(stringTree1.size() == 0);
        assertTrue(!stringTree1.contains("AB"));
        assertTrue(!stringTree1.contains("Bob"));
        integerTestTreeSmall.clear();
        assertEquals(0, integerTestTreeSmall.size());


    }

    @Test
    void contains() {
        stringTree1.add("kirk");
        stringTree1.add("z");
        stringTree1.add("Alfred");
        stringTree1.add("Indumathi");
        stringTree1.add("Zoolander");
        stringTree1.add("Bob");
        stringTree1.add("Shannon");
        stringTree1.add("shannon");
        stringTree1.add("AB");
        stringTree1.add("a");


        assertTrue(stringTree1.contains("shannon"));
        assertTrue(stringTree1.contains("shannon"));
        assertTrue(stringTree1.contains("z"));
        assertTrue(stringTree1.contains("Bob"));
        assertTrue(stringTree1.contains("Indumathi"));
        assertTrue(stringTree1.contains("Alfred"));

        assertTrue(integerTestTreeSmall.contains(5)); //root
        assertTrue(integerTestTreeSmall.contains(1)); //smallest leaf
        assertTrue(integerTestTreeSmall.contains(2)); //two children
        assertTrue(integerTestTreeSmall.contains(4));
        assertTrue(integerTestTreeSmall.contains(6)); //leaf
        assertTrue(integerTestTreeSmall.contains(11)); //farthest right leaf
        assertTrue(integerTestTreeSmall.contains(10));
        assertFalse(integerTestTreeSmall.contains(-7819));
        assertFalse(integerTestTreeSmall.contains(0));

        assertTrue(treeOfOne.contains(50));

    }

    @Test
    void containsAll() {
        stringTree1.add("kirk");
        stringTree1.add("z");
        stringTree1.add("Alfred");
        stringTree1.add("Indumathi");
        stringTree1.add("Zoolander");
        stringTree1.add("Bob");
        stringTree1.add("Shannon");

        lordOfRingsList.addAll(lordArray);
        assertTrue(lordOfRingsList.containsAll(lordArray));
        assertFalse(lordOfRingsList.containsAll(lordArray2));
        lordOfRingsList.addAll(lordArray2);
        assertTrue(lordOfRingsList.containsAll(lordArray2));

        assertTrue(integerTestTreeSmall.containsAll(intSmallArrayListContainsAll));
        assertFalse(integerTestTreeSmall.containsAll(intSmallArrayListContainsAll2));

    }


    @Test
    void first() {
        stringTree1.add("Kirk");
        stringTree1.add("alfred");
        stringTree1.add("Shannon");
        stringTree1.add("Bill");
        stringTree1.add("AA");

        String returnString = stringTree1.first();
        // System.out.println(returnString);

        assertEquals("aa", stringTree1.first());
        assertEquals("aragorn", lordOfRingsList.first());

    }

    @Test
    void isEmpty() {
        lordOfRingsList.clear();
        assertTrue(lordOfRingsList.isEmpty());
        assertEquals(0, lordOfRingsList.size());
        assertTrue(emptyTree.isEmpty());
        assertEquals(0, emptyTree.size());
    }

    @Test
    void last() {
        assertEquals("sam", lordOfRingsList.last());
        lordOfRingsList.add("sauron");
        assertEquals("sauron", lordOfRingsList.last());
        lordOfRingsList.add("zoey");
        assertEquals("zoey", lordOfRingsList.last());
    }

    @Test
    void remove() {
        removeTree.remove("g");
        stringTree3.add("Gimli");
        stringTree3.add("Aragorn");
        stringTree3.add("Legless");
        stringTree3.add("Gandalf");
        stringTree3.add("Zoey");
        stringTree3.add("Sam");
        stringTree3.add("Frodo");
        stringTree3.add("AAronRoger");
        stringTree3.add("aab");
        stringTree3.add("aac");
        stringTree3.add("a");
        stringTree3.add("Bilbo");

        stringTree3.remove("aab");
        assertTrue(!stringTree3.remove("aab"));
        assertFalse(stringTree3.remove("aab"));

        stringTree3.addAll(stringArrayList);

        stringTree3.remove("echo");
        stringTree3.add("bh");
        stringTree3.remove("elron");
        stringTree3.writeDot("STRINGTREE2.dot");

        lordOfRingsList.remove("aragorn");
        assertTrue(!lordOfRingsList.contains("aragorn"));
        assertTrue(lordOfRingsList.contains("legolas"));
        assertTrue(lordOfRingsList.contains("Legolas"));
        lordOfRingsList.remove("legolas");
        assertTrue(!lordOfRingsList.contains("legolas"));


        //int tree
        intTree.remove(375);
        assertFalse(intTree.contains(375));
        intTree.add(426);
        intTree.writeDot("intTree.dot");


        jeffTest.remove(1);


        assertTrue(!jeffTest.contains(1));
        jeffTest.writeDot("JeffTestAfter.dot");

        jeffTestLeft.remove(3);
        jeffTestLeft.writeDot("leftAfter.dot");


        //small int tree stuff
        assertTrue( integerTestTreeSmall.remove(4)); //remove a leaf left
        assertTrue(integerTestTreeSmall.remove(8)); //remove a leaf right
        assertTrue(  integerTestTreeSmall.remove(5)); //remove root
        assertTrue( integerTestTreeSmall.remove(7)); //remove a leaf with two children
        assertTrue(integerTestTreeSmall.add(9)); //extra add
        assertFalse(integerTestTreeSmall.remove(-666)); //cant remove something not there

        integerTestTreeSmall.writeDot("NewIntSmallTree.dot");

    }


    @Test
    void removeAll() {
        //stringArrayList, stringTree2
        //kirk, Sauron, Elron, Aragorn,
        stringTree2.writeDot("BeforeRemoveAll.dot");
        assertTrue(stringTree2.removeAll(stringArrayList));
        stringTree2.writeDot("AfterRemoveAll.dot");

        //aragorn, gollum, frodo, sam
        assertTrue(lordOfRingsList.contains("gollum"));
        lordOfRingsList.removeAll(lordArray);
        assertFalse(lordOfRingsList.contains("gollum"));
        assertTrue(!lordOfRingsList.contains("sam"));
        assertTrue(lordOfRingsList.contains("Gandalf"));

        for (String s : lordArray) {
            assertFalse(lordOfRingsList.contains(s));
        }

        helloWorldTree.writeDot("HelloWorld.dot");

        helloWorldTree.removeAll(helloWorldArrayList);

        helloWorldTree.writeDot("HelloWorldAfter.dot");

        integerTestTreeSmall.removeAll(intSmallArrayListContainsAll);
        assertTrue(!integerTestTreeSmall.containsAll(intSmallArrayListContainsAll2));

    }


    @Test
    void removeAll2() {
        lordOfRingsList.addAll(lordArray2);
        lordOfRingsList.addAll(parksAndRecArrayList);
        lordOfRingsList.writeDot("ComboList.dot");

        lordOfRingsList.removeAll(parksAndRecArrayList);

        lordOfRingsList.writeDot("noParkPeople.dot");

        for (String character : parksAndRecArrayList) {
            assertFalse(lordOfRingsList.contains(character));
        }


        integerTestTreeSmall.removeAll(removeAllIntArrayList);
        for (int i : removeAllIntArrayList) {
            assertFalse(integerTestTreeSmall.contains(i));
        }

    }

    @Test
    void size() {
        //assertEquals(11, lordOfRingsList.size());
        assertEquals(0, sizeTree.size());
        sizeTree.add(100);
        assertEquals(1, sizeTree.size());
        sizeTree.add(5);
        assertEquals(2, sizeTree.size());
        sizeTree.add(4);
        assertEquals(3, sizeTree.size());
        sizeTree.add(-2);
        assertEquals(4, sizeTree.size());
        sizeTree.add(10);
        assertEquals(5, sizeTree.size());
        sizeTree.writeDot("sizeTree.dot");
        assertEquals(11, removeTree.size());

        assertEquals(9, integerTestTreeSmall.size());

        assertEquals(10, lordOfRingsList.size());


    }

    @Test
    void toArrayList() {

        // System.out.println("Tree array");
        ArrayList<Integer> integerArrayList = intTree.toArrayList();
        //System.out.println(integerArrayList);

        ArrayList<String> stringArrayList = stringTree2.toArrayList();
        //System.out.println((stringArrayList));


        ArrayList<Integer> integerArrayList2 = intTree2.toArrayList();
        //System.out.println(integerArrayList2);

        // System.out.println("lord of rings list");
        ArrayList<String> ringList = lordOfRingsList.toArrayList();
        //  System.out.println(ringList);
        ArrayList<Integer> smallIntArrayList = integerTestTreeSmall.toArrayList();
        //System.out.println(smallIntArrayList);


    }
}