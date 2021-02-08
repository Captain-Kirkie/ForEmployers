package assignment05;

//https://www.softwaretestinghelp.com/binary-search-tree-in-java/
//https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
//dot -Tpng expressionTree.dot -o expressionTree.png makes  png of tree
//dot -Tgif expressionTree.dot -o expressionTree.png
//dot -Tpng First_add.dot -o firstAdd.png

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;



public class BinarySearchTree<T extends Comparable<? super T>> implements SortedSet<T> {
    Node root;
    int size;


    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Checks to see if item is already contained in set, addes item to BST elswise
     * @param item
     *          - Item to be addd to the set
     * @return -returns boolean, ture if set was changed because of this call, false elsewise
     * @throws NullPointerException - if item is null
     */
    @Override
    public boolean add(T item) throws NullPointerException { //TODO: Could right this recurively
        if (item == null) {
            throw new NullPointerException();
        }
        if (item instanceof String) {
            item = (T) ((String) item).toLowerCase();
        }
        Node newNode = new Node(item);
        Node tmp = root;
        if (root == null) { //if it the first element make that item
            root = newNode;
            size++;
            return true;
        } else if (!contains(item))
            while (true) {
                if (item.compareTo(tmp.data) < 0) {
                    if (tmp.left == null) {
                        tmp.left = newNode;
                        size++;
                        return true;
                    } else {
                        tmp = tmp.left;
                    }
                } else {
                    if (tmp.right == null) {
                        tmp.right = newNode;
                        size++;
                        return true;
                    } else {
                        tmp = tmp.right;
                    }
                }
            }
        return false; //if contains item return false
    }
    /**
     * Ensures that this set contains all items in the specified collection.
     *
     * @param items
     *          - the collection of items whose presence is ensured in this set
     * @return true if this set changed as a result of this method call (that is, if
     *         any item in the input collection was actually inserted); otherwise,
     *         returns false
     * @throws NullPointerException
     *           if any of the items is null
     */

    @Override
    public boolean addAll(Collection<? extends T> items) {

        for (T item : items) {
            if(item == null){
                throw new NullPointerException();
            }else{
                if(!add(item)){
                    return false;
                }
            }
        }
      return true;
    }

    /**
     * sets top root of set to null
     * clears set
     */
    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     *
     * @param item
     *          - the item sought in this set
     * @return boolean value true if item contained in set, false elswise
     */

    @Override
    public boolean contains(T item) {
        if(item == null){
            throw new NullPointerException();
        }

        if (item instanceof String) {
            item = (T) ((String) item).toLowerCase();
        }
        Node tmp = root;
        while (tmp != null) {
            if (tmp.data.compareTo(item) == 0) { //if it is the item
                return true;
            } else if (tmp.data.compareTo(item) < 0) { //if item is greater than
                tmp = tmp.right;
            } else { //if item is not greater than tmp, its less than, go right
                tmp = tmp.left;
            }
        }
        return false;
    }

    /**
     * Itterates through
     * @param items
     *          - the collection of items sought in this set
     * @return returns true if all items are contained in the set, false if any items are not contained in set
     */
    @Override
    public boolean containsAll(Collection<? extends T> items) {
        for (T item : items) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns furthest left(Smallest item)
     *
     * @return
     * @throws NoSuchElementException
     */
    @Override
    public T first() throws NoSuchElementException {
        if (root == null) {
            throw new NoSuchElementException();
        }

        Node tmp = root;
        while (tmp.left != null)
            tmp = tmp.left;

        return tmp.data;
    }

    /**
     *
     * @return returns whether this BST is empty of nodes and data
     */
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return returns largest item in the set
     * @throws NoSuchElementException if the set/root of set is null
     */
    @Override
    public T last() throws NoSuchElementException {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node tmp = root;
        while (tmp.right != null) {
            tmp = tmp.right;
        }
        return tmp.data;
    }

    /**
     *
     * @param item
     *          - the item whose absence is ensured in this set
     * @return
     *      -returns boolean wheather or not the set was changed because of this function call
     */
    @Override
    public boolean remove(T item) {
        if (item instanceof String) {
            item = (T) ((String) item).toLowerCase();
        }

        if(this.size == 1 && this.root.data.compareTo(item) == 0){
           clear();
        }

        if (contains(item)) {
            deleteNode(this.root, item);
            size--;
            return true;
        }
        return false;
    }

    /**
     * recursively deletes node in a tree
     * @param node node that recursivly gets passed through while searching for value
     * @param value value to be deleted
     * @return returns boolean depending on whether or not the set was changed as a result of this call
     */

    //https://medium.com/swlh/java-how-to-delete-a-node-in-binary-search-tree-aa2d4befe728
    //trying to do a recursive delete
    public Node deleteNode(Node node, T value) {
        if (node == null)
            return node;

        if (value.compareTo(node.data) > 0) { //move right if greater
            node.right = deleteNode(node.right, value);
//            System.out.println("Value greater than");
        } else if (value.compareTo(node.data) < 0) { //move left if less than
//            System.out.println("value less than");
            node.left = deleteNode(node.left, value);
        } else { //found target
//            System.out.println("Value found");
            if (node.left == null && node.right == null) { //leaf node
                node = null;
            } else if (node.right != null) { //node has one child grabs biggest node to right
                node.data = successor(node); //node equals the biggest, then we delete the duplicate
                node.right = deleteNode(node.right, node.data);
            } else { //node has two children
                node.data = predecessor(node); //biggest value down right == node
                node.left = deleteNode(node.left, node.data); //delete duplicate
            }
        }
        return node;
    }
    //helper function for remove
    private T predecessor(Node node) { //find the biggest value down the right
        node = node.left; //equals th smaller node
        while (node.right != null) {
            node = node.right;
        }
        return node.data;
    }
    //helper function for remove
    private T successor(Node node) {
        node = node.right; //go down bigger one
        while (node.left != null) { //grab the smallest
            node = node.left;
        }
        return node.data;
    }

    /**
     *
     * @param items
     *          - the collection of items whose absence is ensured in this set
     * @return
     *      -boolean returned if this set was changed as a result of this function call
     */
    @Override
    public boolean removeAll(Collection<? extends T> items) {
        boolean itemExist = false;
        for (T item : items) {
            if (items.contains(item)) {
                remove(item);
                itemExist = true;
            }
        }
        return itemExist;
    }

    /**
     *
     * @return -the number of items in the set
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     *
     * @return an array of ordered values in the set
     * recursively calls itself inorder
     */

    @Override
    public ArrayList<T> toArrayList() { //TODO
        ArrayList<T> arrayList = new ArrayList<>();

        addToArrayList(arrayList, this.root);

        return arrayList;
    }

    //recursive function to make array list
    private void addToArrayList(ArrayList<T> arrayList, Node node){
            if(node == null)
                return;

            //in order traversal
            addToArrayList(arrayList, node.left);

            arrayList.add(node.data);

            addToArrayList(arrayList, node.right);
    }

    //helper functions
    public void printTreeInOrder(){
        treePrintInOrder(this.root);
    }
    private void treePrintInOrder(Node node){
        if(node == null)
            return;

        treePrintInOrder(node.left);

        System.out.println(node.data);

        treePrintInOrder(node.right);


    }

    public void printTreePreOrder(){
        treePrintInOrder(this.root);

    }
    private void treePrintPreOrder(Node node){
        if(node == null)
            return;

        System.out.println(node.data);

        treePrintPreOrder(node.left);

        treePrintPreOrder(node.right);

    }


    /**
     * used to write to a gif file
     * @param filename -file name to be created
     */
    public void writeDot(String filename) {
        try {
            PrintWriter output = new PrintWriter(new FileWriter(filename));
            output.println("graph g {");
            if (root != null)
                output.println(root.hashCode() + "[label=\"" + root.data + "\"]");
            writeDotRecursive(root, output);
            output.println("}");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Recursively traverse the tree, outputting each node to the .dot file
    private void writeDotRecursive(BinarySearchTree.Node n, PrintWriter output) throws Exception {
        if (n == null)
            return;
        if (n.left != null) {
            output.println(n.left.hashCode() + "[label=\"" + n.left.data + "\"]");
            output.println(n.hashCode() + " -- " + n.left.hashCode());
        }
        if (n.right != null) {
            output.println(n.right.hashCode() + "[label=\"" + n.right.data + "\"]");
            output.println(n.hashCode() + " -- " + n.right.hashCode());
        }

        writeDotRecursive(n.left, output);
        writeDotRecursive(n.right, output);
    }


    class Node {
        T data; //data to be compared
        Node left, right; //pointers to left and right node

        public Node(T Data) { //constructor for node
            data = Data;
            left = right = null;
        }


    }


}
