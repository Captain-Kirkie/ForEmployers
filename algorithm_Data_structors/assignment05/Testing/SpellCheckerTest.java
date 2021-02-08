package assignment05.Testing;

import assignment05.BinarySearchTree;
import assignment05.Demos.SpellCheckerDemo;
import assignment05.SpellChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


class SpellCheckerTest {
    File myFile = new File("src/assignment05/dictionary.txt"); //get dictionary
    //SpellChecker dictionary1 = new SpellChecker(myFile);
    SpellChecker dictionary2 = new SpellChecker();
    SpellChecker dictionary3 = new SpellChecker(new File("src/assignment05/dictionary.txt"));
    ArrayList<String> dictionaryArrayList1 = new ArrayList<>(); //TODO: figure this out
    SpellChecker getDictionary4 = new SpellChecker(dictionaryArrayList1); //TODO: Figure this out?




    @BeforeEach
    void setup() {
        dictionary2.addToDictionary("Kirk");
        dictionary2.addToDictionary("Shannon");
        dictionary2.addToDictionary("Jackass");
        dictionary2.addToDictionary("Blueberry");
        dictionary2.addToDictionary("huckleberry");
        dictionary2.addToDictionary("hailmary");
        dictionary2.addToDictionary("Fart");
        dictionary2.addToDictionary("Chex");
        dictionary2.addToDictionary("sara");


    }


    @AfterEach
    void reset(){
        dictionary2 = null;
        dictionary3 = null;

    }

    @Test
    void addToDictionary() {
        // dictionary1.addToDictionary("Kirk");
        // System.out.println(dictionary1);
       // System.out.println(dictionary2);
        //System.out.println(dictionary3);
        dictionary3.addToDictionary("Kirk");
        dictionary3.addToDictionary("Bilbo");
        BinarySearchTree<String> containsTree = dictionary3.getDictionary();
        assertTrue(containsTree.contains("kirk"));
        assertTrue(containsTree.contains("Bilbo"));
        assertFalse(containsTree.contains("tellamarkeiter"));
        System.out.println(containsTree.size());



    }

    @Test
    void removeFromDictionary() {
        dictionary2.removeFromDictionary("Fart");
        BinarySearchTree<String> dictTree2 = dictionary2.getDictionary();
        ArrayList<String> dicArr2 = dictTree2.toArrayList();
        System.out.println(dicArr2);

        dictionary3.addToDictionary("supercalifragilisticexpialidocious");
        dictionary3.removeFromDictionary("zigzag");
        BinarySearchTree<String> dictionary3Tree = dictionary3.getDictionary();
        assertFalse(dictionary3Tree.contains("zigzag"));
        assertFalse(dictionary3Tree.contains("Zigzag"));
        assertTrue(dictionary3Tree.contains("supercalifragilisticexpialidocious"));

        dictionary3.removeFromDictionary("supercalifragilisticexpialidocious");
        BinarySearchTree<String> removeTree = dictionary3.getDictionary();
        assertTrue(!removeTree.contains("supercalifragilisticexpialidocious"));



    }

    @Test
    void spellCheck() {
        SpellCheckerDemo.run_spell_check(dictionary3, "src/assignment05/test.txt");






    }
}