package assignment03;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementing this Iterable interface allows an object to be
 * the target of the "for-each loop" statement.
 *
 * @param <E> needs to impliment Iterable in order to use the Iterator
 *            cant add duplicates
 */
//can impliment comprable, or make own compartor that makes natural ording


public class BinarySearchSet<E> implements SortedSet<E>, Iterable<E> {
    private E[] array; //create a new array Start at 100
    private int size;
    private int capacity;
    Comparator comparator;
    //Comparator<? super E> comparator;

    /**
     * if used, assumed that elements are in natural ordering
     */
    public BinarySearchSet() {
        this.size = 0; //initially the array is empty
        this.capacity = 10; //initial capacity
        this.array = (E[]) new Object[capacity]; //creates a new array of capacity
        this.comparator = Comparator.nullsLast(Comparator.naturalOrder()); //assumes null is greater than non-null
        //returns compartor that considers null to be less than all non-null values
    }


    /**
     * if this constructor is used, it is assumed elements are sorted using provided comparator
     *
     * @param comparator
     */
    public BinarySearchSet(Comparator<? super E> comparator) {
        this.size = 0; //initially the array is empty
        this.capacity = 10; //initial capacity
        this.array = (E[]) new Object[capacity]; //creates a new array of capacity
        this.comparator = comparator;
    }


    @Override //return the comparator for the specified type
    public Comparator<E> comparator() {
        return this.comparator;
    }

    @Override
    public E first() throws NoSuchElementException {
       if(this.size == 0){
           throw new NoSuchElementException();
       }
        return array[0];
    }

    @Override
    public E last() throws NoSuchElementException {
        if(this.size == 0){
            throw new NoSuchElementException();
        }
        return array[this.size - 1];
    }


    /**
     * @param element element to be added to this set
     * @return check to see if it contain element, if it does dont add
     * search through to find where element to add will go
     * shift all other elements as appropriate
     * add element to appropriate positions
     * <p>
     * check if there is room,
     * if not grow.....
     */
    //TODO: ALWAYS GOING TO INSERT AT MID + 1 USING BINARY SEARCH
    @Override //inserts items in correct order so they are sorted
    public boolean add(Object element) { //uses binary search
        //TODO:add stuff to array at correct position
        E itemToAdd = (E) element;
        int index = binarySearchDriver(array, itemToAdd); //returns index, or index before needs to be added
        if (index >= 0 && this.comparator.compare(array[index], element) == 0) {
            return false; //The element already exists in array
        } else {
            int positionToInsert = index + 1; //insert at mid + 1
            //System.out.println("Position to insert " + positionToInsert);
            if (capacity <= size + 1) {
                //TODO: does the array need to grow? if so grow then add to array
                growArray();
            }
            for (int i = size - 1; i >= positionToInsert; i--) { //shift array over and insert element in searchReturn + 1
                this.array[i + 1] = this.array[i]; //shift everything over
            }
            array[positionToInsert] = itemToAdd; //this is where the element needs to go, but everything needs to be shifted
            //System.out.println("inserting this " + array[positionToInsert] + " To position " + positionToInsert);
            this.size++; //update the size
            //System.out.println("This is the end of the array ");
//            for (int i = 0; i < size; i++) {
//                System.out.println("This is what is in array " + this.array[i]);
//            }
            return true;
        }
    }

    //grows the array
    private void growArray() { //TODO: TEST THIS
        //create new array of double the capacity
        this.capacity = this.capacity * 2;
        E[] doubledArray = (E[]) new Object[capacity];
        for (int i = 0; i < this.size; i++) {
            doubledArray[i] = this.array[i];
        }
        this.array = doubledArray;
    }


    @Override
    public boolean addAll(Collection elements) { //TODO: TEST THIS THIS is not currently working!
        int origionalSize = this.size;
        int origionalCapacity = this.capacity;
        for (Object element : elements) { //if set changes, return true else return false
            this.add(element); //TODO: Get this to work
        }
        if (this.size != origionalSize || this.capacity != origionalCapacity) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.array[i] = null;
        }
        this.size = 0;
    }

    @Override //use recursive binary searchv //TODO: Fix this seems wrong
    public boolean contains(Object element) { //set is already sorted
        int returnIndex = binarySearchDriver(this.array, (E) element);
        if (this.array[returnIndex].equals(element)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        if (this.size == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * returns an iterator for the set
     *
     * @return
     */
    @Override
    public Iterator iterator() { //way to return an itterator
        return new BinarySearchSetIterator();
        //return null;
    }

    @Override
    public boolean remove(Object element) { //TODO: impliment this
        E elementCasted = (E) element; //is this an issue?
        if (this.size == 0 || !contains(elementCasted)) {
            return false;
        } else {
            int target = this.binarySearchDriver(this.array, elementCasted);
            for (int i = target; i < size - 1; i++) {
                this.array[i] = this.array[i + 1];
            }
            this.array[size - 1] = null;
            this.size--;
            return true;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override //TODO: TEST THIS
    public Object[] toArray() { //TODO: can just return this.array
        Object[] returnArray = new Object[capacity];
        for (int i = 0; i < size; i++) {
            returnArray[i] = this.array[i];
        }
        return returnArray;
    }

    @Override
    public boolean removeAll(Collection elements) {
        if (elements.size() <= 0) {
            return false;
        } else {
            for (Object obj : elements) {
                remove(obj);
            }
            return true;
        }
    }

    @Override
    public boolean containsAll(Collection elements) {
        for (Object obj : elements) { //TODO: Should use contains on this?
            int binSearchPos = binarySearchDriver(this.array, (E) obj); //TODO: This may have some edge cases
            if (binSearchPos == -1 || !obj.equals(array[binSearchPos])) {
                return false;
            }
        }
        return true;
    }

    /**
     * create a binary search method
     * returns index of goal
     * otherwise -1
     *
     * @param array
     * @return
     */
    //TODO TESTS THIS/
    // need a comparator
    private int binarySearch(E[] array, E goal, int high, int low) {
        int mid = -1;
        if (high >= low) {
            mid = low + (high - low) / 2;
            if (this.comparator.compare(goal, array[mid]) == 0) { //Why did it break here
                return mid;
            } else if (this.comparator.compare(goal, array[mid]) < 0) {
                return binarySearch(array, goal, mid - 1, low);//search left side of array
            } else {
                //System.out.println("else statment in binary search low " + (mid + 1)+ " high " + high);
                return binarySearch(array, goal, high, mid + 1);
            }
        }
        return high;
    }

    public int binarySearchDriver(E[] array, E target) {
        //check that conditions are met
        //if not return null..or somethings
        int high = this.size - 1;
        int low = 0; //if the array is empty, return -1 indice
        if (this.size == 0) { //TODO: Verify this is correct
            return -1;
        }
        return binarySearch(array, target, high, low);
    }

    /**
     * This is the itterator used to itterate through binary search set
     *
     * @param <E>
     */
    private class BinarySearchSetIterator<E> implements Iterator<E> {
        private int position; //position of the iterator

        @Override //checks to see if there is more array to access
        public boolean hasNext() { //TODO: TEST THIS
            if (position < size)
                return true;
            else
                return false;
        }

        @Override //TODO: TEST THIS
        public E next() {
            E obj = (E) array[position]; //return the position in the array
            position++;
            return obj;
        }

        @Override //TODO:Should remove element last seen
        public void remove() { //TODO: Shouldnt be able to call remove twice in a row?
            BinarySearchSet.this.remove(array[position]);
            position --;
        }
    }


    //getter method
    public int getCapacity() {
        return this.capacity;
    }


    public E[] getArray() {
        return this.array;
    }
}

//
//    @Override //inserts items in correct order so they are sorted
//    public boolean add(Object element) { //uses binary search
//        //TODO:add stuff to array at correct position
//        if(binarySearchDriver(array, (E) element) > -1){
//            return false;
//        }
//        for (int i = 0; i < this.array.length; i++) { //might not nee
//            if (this.comparator.compare(element, array[i]) == 1) { //TODO: shift element over and insert
//                int position = i; //this is where we insert
//                E itemToCopy = this.array[i]; // copy initial position
//                for (int k = i; k < size; k++) { //shift everything right
//                    E itemToSave = array[k + 1]; //save next position
//                    array[k + 1] = itemToCopy; // next position = item you copied originially
//                    itemToCopy = itemToSave;
//                }
//                //TODO shift everything over
//                //add stuff
//                this.array[position] = (E) element;
//            }
//        }
//        this.size++;
//        return true;
//    }