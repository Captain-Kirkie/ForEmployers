package lab05;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<E> implements List<E>, Iterable<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    //getters
    public Node<E> getHead() {
        return this.head;
    }

    public Node<E> getTail() {
        return this.tail;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator();
    }

    /**
     * Inserts the specified element at the beginning of the list. O(1) for a
     * doubly-linked list.
     * newNode.next = head //the next pointer of this new node points to whatever was the head
     * previous pointer is null
     */
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<E>(element); //Need to cast?
        newNode.next = head;
        newNode.prev = null;

        if (head != null) { //if the head exist make the head point to the new node.
            head.prev = newNode;
        }

        if (size == 0) {
            head = newNode;
            tail = newNode;
        }

        head = newNode; //set the member variable as the new nod


        this.size++; //update size
    }


    /**
     * Inserts the specified element at the end of the list. O(1) for a
     * doubly-linked list.
     */
    @Override
    public void addLast(E o) {
        Node<E> newNode = new Node<E>(o);

        Node<E> last = head;

        newNode.next = null; //last node so null pointer

        //If the list is empty, make this the head
        if (head == null) {
            newNode.prev = null;
            head = newNode;
            tail = newNode; //head and tail both equal the new node
            size++;
            return;
        }

        //else traverse till last node
        while (last.next != null) { //TODO: This is how we will travers
            last = last.next;
        }

        //change the next of the last node
        last.next = newNode;

        //make last node as previous of the new node
        newNode.prev = last;
        this.tail = newNode;
        size++;

    }

    /**
     * add an element to the specified index,
     * searches from tail if index is closer to tail
     * searches from head if index is closer to head
     *
     * @param index
     * @param element
     * @throws IndexOutOfBoundsException
     */

    @Override //TODO: Start here Finish this!
    public void add(int index, E element) throws IndexOutOfBoundsException {

        if (head == null || index == 0) { //if this is empty add to the front
            addFirst(element);
            return;
        } else if (index == size) { //if they want to add to end
            addLast(element);
            return;
        }
        Node<E> newNode = new Node<E>(element); //create a new node with index data
        Node<E> temp;
        //check size, if index to insert is closer to end, start at tail
        //start at head
        if (index < (size - 1) / 2) {
            temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next; //get to the position we want
            }
            newNode.next = temp; //points to one after position i want
            newNode.prev = temp.prev; //sets newNode pointer to the one before the poisition
            temp.prev.next = newNode; //one befores next pointer and points it to new node
        } else {
            System.out.println("Its on the other half of the array!");
            temp = tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.prev; //get to the position we want
            }
            newNode.next = temp;
            newNode.prev = temp.prev;
            temp.prev = newNode;
            temp.prev.next = newNode;
        }
        size++;
    }

    /**
     * returns data at the head of the list
     *
     * @return
     * @throws NoSuchElementException
     */
    @Override
    public E getFirst() throws NoSuchElementException {
        return head.data;
    }

    /**
     * retuns data at the tail of the list
     *
     * @return
     * @throws NoSuchElementException
     */
    @Override
    public E getLast() throws NoSuchElementException {
        return tail.data;
    }


    /**
     * returns the value at the input index
     * starts at head if index is closer to head
     * starts at tail if index is closer to tail
     *
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        Node<E> temp;
        if (index > size - 1) {
            return null;
        }

        if (index < (size - 1) / 2) {
            temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next; //loop through to get where I want to go
            }
            return temp.getData();
        } else {
            temp = tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.prev;
            }
            return temp.getData();
        }
    }

    /**
     * removes frist element in linked list
     * sets element next to head  prev pointer to null
     * Makes next element head
     * make previous head next null
     *
     * @return
     * @throws NoSuchElementException
     */

    @Override
    public E removeFirst() throws NoSuchElementException {
        if (head == null || size == 0) {
            System.out.println("Nothing to return/remover");
            return null;
        }
        E data = head.data; //save this and reutrn int at the end
        head = head.next; // make head the next in line.
        if (!(size <= 1)) {
            head.prev = null;
        }
        size--;
        return data;
    }

    @Override
    public E removeLast() throws NoSuchElementException {
        if (head == null || size == 0) {
            System.out.println("Nothing to return/remover");
            return null;
        } else {
            E data = tail.data; //got data
            if (!(size == 1)) {
                tail = tail.prev;
                tail.next = null;
            }
            size--;
            return data;
        }
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index == size - 1) {
            return removeLast();
        } else if (index == 0) {
            return removeFirst();
        }
        //TODO: Get next data and return it
        Node<E> temp;
        temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next; //temp now equals the index we want to remove
        }
        E data = temp.data;
        temp.next.prev = temp.prev;
        temp.prev.next = temp.next;
        temp.next = null;
        temp.prev = null;

        size--;
        return data;
    }

    @Override
    public int indexOf(E element) {
        Node<E> tmp = head;
        for (int i = 0; i < size; i++) {
            if (tmp.data == element) {
                return i;
            } else {
                tmp = tmp.next;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E element) {
        Node<E> tmp = tail;
        for (int i = size - 1; i > 0; i--) {
            if (tmp.data == element) {
                return i;
            } else {
                tmp = tmp.prev;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        if (this.size == 0)
            return true;
        else
            return false;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
    }

    @Override
    public Object[] toArray() {
        Object[] returnArray = new Object[size];
        Node<E> tmpNode = head;
        for (int i = 0; i < size; i++) {
            returnArray[i] = tmpNode.data;
            tmpNode = tmpNode.next;
        }
        return returnArray;
    }


    //TODO: Impliment these one needs to be itterable, and one needs to impliment itterator
//    @Override
//    public boolean hasNext() {
//        return false;
//    }
//
//    @Override
//    public E next() {
//        return null;
//    }
//
//    @Override
//    public void remove() {
//
//    }


    private class DoublyLinkedListIterator implements Iterator<E> { //TODO: Iterator skipping one
        Node<E> current;
        int index;
        Node<E> previous;

        public DoublyLinkedListIterator() {
            current = head;
            index = 0;
        }

        @Override //checks to see if there is more array to access
        public boolean hasNext() { //TODO: TEST THIS
            if (index < size) { //is this size minus 1?
                return true;
            } else {
                return false;
            }
        }

        @Override
        public E next() {
            if (hasNext()) { //if returns true
                Node tmp = current; //save the current to return later
                previous = current; // save the previous nodes position
                current = current.next; //move the current
                index++; //update teh index
                return (E) tmp.data; //Return data
            }else throw new NoSuchElementException();

        }

        @Override
        public void remove() { //TODO: Idk if this is working
            DoublyLinkedList.this.remove(index - 1);
            index --;

        }
    }

    public class Node<E> {
        private E data;
        public Node<E> next; //TODO: Make next and previous methods??
        public Node<E> prev;

        public Node(E data) {
            this.next = null;
            this.prev = null;
            this.data = data;
        }

        public E getData() {
            return this.data;
        }


    }


}
