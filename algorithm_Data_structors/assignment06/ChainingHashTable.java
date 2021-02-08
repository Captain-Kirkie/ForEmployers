package assignment06;
//https://www.geeksforgeeks.org/implementing-our-own-hash-table-with-separate-chaining-in-java/
/*
http://www.cse.yorku.ca/~oz/hash.html (Links to an external site.)
https://stackoverflow.com/questions/7666509/hash-function-for-string (Links to an external site.)
https://stackoverflow.com/questions/2624192/good-hash-function-for-strings (Links to an external site.)
http://cseweb.ucsd.edu/~kube/cls/100/Lectures/lec16/lec16-12.html (Links to an external site.)
 */

//https://www.sanfoundry.com/java-program-implement-hash-tables-chaining-singly-linked-list/
//https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

//TODO: Could impliment growing... Rehash and reassign values
//TODO: Could put a tree into the hash table instead of a linked list

public class ChainingHashTable implements Set<String> {

    private int capacity; //how big is the array
    private HashFunctor functor;
    private LinkedList<String>[] storage;
    private int size; //how many items are currently in the array
    private int collision;

    @SuppressWarnings("unchecked") //constructor, Functor used to determine which hash value to use
    public ChainingHashTable(int capacity, HashFunctor functor) {
        this.capacity = capacity; //capacity/size of list
        this.functor = functor; //to be used to calculate hash value
        this.storage = (LinkedList<String>[]) new LinkedList[capacity]; //create an array of linked lists. Cast it
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public ChainingHashTable(HashFunctor functor, File documentFile) {
        ArrayList<String> words = readFromFile(documentFile);
        this.capacity = (words.size() * 2);
        this.size = 0;
        this.functor = functor;
        this.storage = (LinkedList<String>[]) new LinkedList[capacity];
        this.collision = 0;
        this.addAll(words);
    }

    /**
     * Ensures that this set contains the specified item.
     *
     * @param item - the item whose presence is ensured in this set
     * @return true if this set changed as a result of this method call (that is, if
     * the input item was actually inserted); otherwise, returns false
     */

    @Override
    public boolean add(String item) {
        int index = findIndexHashed(item); //find index of item
        if (storage[index] == null) {
            storage[index] = new LinkedList<String>();
        } else {
            collision++; //keeping track of collisions
        }

        if (!storage[index].contains(item)) { //if item is not already contained in linked list
            this.size++;
            return storage[index].add(item);
        } else {
            return false;
        }
    }

    /**
     * Ensures that this set contains all items in the specified collection.
     *
     * @param items - the collection of items whose presence is ensured in this set
     * @return true if this set changed as a result of this method call (that is, if
     * any item in the input collection was actually inserted); otherwise,
     * returns false
     */

    @Override
    public boolean addAll(Collection<? extends String> items) {
        boolean added = false;
        for (String s : items) {
            if (add(s)) //if added returns true. Set boolean to true
                added = true;
        }
        return added;
    }

    /**
     * Removes all items from this set. The set will be empty after this method
     * call.
     * Sets indices back to null
     */

    @Override
    public void clear() {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null) {
                storage[i].clear();
                storage[i] = null;
            }
        }
        this.size = 0;
    }

    /**
     * Determines if there is an item in this set that is equal to the specified
     * item.
     *
     * @param item - the item sought in this set
     * @return true if there is an item in this set that is equal to the input item;
     * otherwise, returns false
     */

    @Override
    public boolean contains(String item) {
        int index = findIndexHashed(item);
        if (storage[index] != null && storage[index].contains(item))
            return true;
        else
            return false;
    }

    /**
     * Determines if for each item in the specified collection, there is an item in
     * this set that is equal to it.
     *
     * @param items - the collection of items sought in this set
     * @return true if for each item in the specified collection, there is an item
     * in this set that is equal to it; otherwise, returns false
     */

    @Override
    public boolean containsAll(Collection<? extends String> items) {
        boolean bool = true;
        for (String s : items) {
            if (!this.contains(s)) {
                bool = false;
            }
        }
        return bool;
    }

    /**
     * Returns true if this set contains no items.
     */

    @Override
    public boolean isEmpty() {
        if (this.size == 0)
            return true;
        else
            return false;
    }

    /**
     * Ensures that this set does not contain the specified item.
     *
     * @param item - the item whose absence is ensured in this set
     * @return true if this set changed as a result of this method call (that is, if
     * the input item was actually removed); otherwise, returns false
     * decrease the size
     */

    @Override
    public boolean remove(String item) {
        int index = findIndexHashed(item);
        if (this.storage[index] != null && this.storage[index].contains(item)) {
            this.size--;
            this.storage[index].remove(item);

            if (this.storage[index].isEmpty())
                this.storage[index] = null;

            return true;

        } else {
            return false;
        }
    }

    /**
     * Ensures that this set does not contain any of the items in the specified
     * collection.
     *
     * @param items - the collection of items whose absence is ensured in this set
     * @return true if this set changed as a result of this method call (that is, if
     * any item in the input collection was actually removed); otherwise,
     * returns false
     */

    @Override
    public boolean removeAll(Collection<? extends String> items) {
        boolean bool = false;
        for (String s : items) {
            if (remove(s)) {
                bool = true;
            }
        }
        return bool;
    }

    /**
     * Returns the number of items in this set.
     */
    @Override
    public int size() {
        return this.size;
    }

    //get number of collisions
    public int getCollision() {
        return this.collision;
    }

    //get has value function
    public int findIndexHashed(String string) {
        return Math.abs((functor.hash(string) % capacity)); //return the index
    }

    //get a specific index
    public LinkedList<String> getLinkedIndex(int index) {
        return storage[index];
    }

    //extra to get storage array
    public LinkedList<String>[] getStorage() {
        return this.storage;
    }

    //helper function to print the list array
    public static void printHashTable(ChainingHashTable table) { //Could maybe do without calling get storage
        LinkedList<String>[] listArr = table.getStorage();
        for (int i = 0; i < listArr.length; i++) {
            System.out.println(listArr[i]);
            if(listArr[i] == null){
            }
        }
    }


    //function tro read in from a text file
    private ArrayList<String> readFromFile(File file) {
        ArrayList<String> words = new ArrayList<>();
        try (Scanner fileInput = new Scanner(file)) {
            fileInput.useDelimiter("\\s*[^a-zA-Z]\\s*"); //any word or char between two spaces \s == space

            while (fileInput.hasNext()) {
                String s = fileInput.next();
                if (!s.equals("")) {
                    words.add(s.toLowerCase());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File " + file + " cannot be found");
        }
        return words;
    }

    /* another scanner option
    Scanner sc = new Scanner(new File("dictionary.txt"));
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
     */
}
