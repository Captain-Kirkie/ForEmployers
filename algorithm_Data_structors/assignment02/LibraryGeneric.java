package assignment02;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class LibraryGeneric<Type> { //TODO: Need to add wildCard?? <? extends Book> ??

    private ArrayList<LibraryBookGeneric<Type>> library;

    public LibraryGeneric() {
        library = new ArrayList<LibraryBookGeneric<Type>>();
    }


    /**
     * Add the specified book to the library, assume no duplicates.
     *
     * @param isbn   -- ISBN of the book to be added
     * @param author -- author of the book to be added
     * @param title  -- title of the book to be added
     */
    public void add(long isbn, String author, String title) {
        library.add(new LibraryBookGeneric<Type>(isbn, author, title));
    }

    /**
     * Add the list of library books to the library, assume no duplicates.
     *
     * @param list -- list of library books to be added
     */
    public void addAll(ArrayList<LibraryBookGeneric<Type>> list) {
        library.addAll(list);
    }

    /**
     * Add books specified by the input file. One book per line with ISBN, author,
     * and title separated by tabs.
     * <p>
     * If file does not exist or format is violated, do nothing.
     *
     * @param filename
     */
    public void addAll(String filename) {
        ArrayList<LibraryBookGeneric<Type>> toBeAdded = new ArrayList<LibraryBookGeneric<Type>>();

        try (Scanner fileIn = new Scanner(new File(filename))) {

            int lineNum = 1;

            while (fileIn.hasNextLine()) {
                String line = fileIn.nextLine();

                try (Scanner lineIn = new Scanner(line)) {
                    lineIn.useDelimiter("\\t");

                    if (!lineIn.hasNextLong()) {
                        throw new ParseException("ISBN", lineNum);
                    }
                    long isbn = lineIn.nextLong();

                    if (!lineIn.hasNext()) {
                        throw new ParseException("Author", lineNum);
                    }
                    String author = lineIn.next();

                    if (!lineIn.hasNext()) {
                        throw new ParseException("Title", lineNum);
                    }
                    String title = lineIn.next();
                    toBeAdded.add(new LibraryBookGeneric<Type>(isbn, author, title));
                }
                lineNum++;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage() + " Nothing added to the library.");
            return;
        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage() + " formatted incorrectly at line " + e.getErrorOffset()
                    + ". Nothing added to the library.");
            return;
        }

        library.addAll(toBeAdded);
    }

    /**
     * Returns the holder of the library book with the specified ISBN.
     * <p>
     * If no book with the specified ISBN is in the library, returns null.
     *
     * @param isbn -- ISBN of the book to be looked up
     */
    public Type lookup(long isbn) { //TODO: is this returning the right type??
        for (LibraryBookGeneric book : library) {
            if (book.getIsbn() == isbn) {
                return (Type) book.getHolder(); //Cast it to whatever type
            }
        }
        return null;
    }

    /**
     * Returns the list of library books checked out to the specified holder.
     * <p>
     * If the specified holder has no books checked out, returns an empty list.
     *
     * @param holder -- holder whose checked out books are returned
     */
    public ArrayList<LibraryBookGeneric<Type>> lookup(Type holder) {
        ArrayList<LibraryBookGeneric<Type>> holderBooks = new ArrayList<>();
        for (LibraryBookGeneric book : library) {
            if (book.getHolder() == holder) { //vs .equals??
                holderBooks.add(book);
            }
        }
        return holderBooks;
    }

    /**
     * Sets the holder and due date of the library book with the specified ISBN.
     * <p>
     * If no book with the specified ISBN is in the library, returns false.
     * <p>
     * If the book with the specified ISBN is already checked out, returns false.
     * <p>
     * Otherwise, returns true.
     *
     * @param isbn   -- ISBN of the library book to be checked out
     * @param holder -- new holder of the library book
     * @param month  -- month of the new due date of the library book
     * @param day    -- day of the new due date of the library book
     * @param year   -- year of the new due date of the library book
     */
    public boolean checkout(long isbn, Type holder, int month, int day, int year) {
        for (LibraryBookGeneric book : library) {
            if (book.getIsbn() == isbn && !book.isCheckedOut()) { //book is here and not checked out
                book.checkBookOut(holder, day, month, year); //check the book out
                return true;
            }
        }
        return false;
    }

    /**
     * Unsets the holder and due date of the library book.
     * <p>
     * If no book with the specified ISBN is in the library, returns false.
     * <p>
     * If the book with the specified ISBN is already checked in, returns false.
     * <p>
     * Otherwise, returns true.
     *
     * @param isbn -- ISBN of the library book to be checked in
     */
    public boolean checkin(long isbn) {
        for (LibraryBookGeneric book : library) {
            if (book.getIsbn() == isbn) {
                book.checkBookIn();
                return true;
            }
        }
        return false;
    }

    /**
     * Unsets the holder and due date for all library books checked out be the
     * specified holder.
     * <p>
     * If no books with the specified holder are in the library, returns false;
     * <p>
     * Otherwise, returns true.
     *
     * @param holder -- holder of the library books to be checked in
     */
    public boolean checkin(Type holder) {
        int bookCount = 0;
        for (LibraryBookGeneric book : library) {
            if (book.getHolder() == holder) {
                book.checkBookIn();
                bookCount++;
            }
        }
        if (bookCount == 0)
            return false;
        else
            return true;
    }

    public int getLibSize() {
        return library.size();
    }

    public ArrayList<LibraryBookGeneric<Type>> getLibList() {
        return this.library;
    }


    /**
     * Returns the list of library books, sorted by ISBN (smallest ISBN first).
     */
    public ArrayList<LibraryBookGeneric<Type>> getInventoryList() {
        ArrayList<LibraryBookGeneric<Type>> libraryCopy = new ArrayList<LibraryBookGeneric<Type>>();
        libraryCopy.addAll(library);

        OrderByIsbn comparator = new OrderByIsbn();

        sort(libraryCopy, comparator);

        return libraryCopy;
    }

    /**
     * Returns the list of library books, sorted by author
     * Creates a new array list of whatever type,
     * adds all inventory from library to that array list
     * sorts based on a compartor implimented below
     * returns sorted arrayList
     */
    //Sorts by Authors full name, if author is the same, tie broken by comparing book title
    public ArrayList<LibraryBookGeneric<Type>> getOrderedByAuthor() {
        ArrayList<LibraryBookGeneric<Type>> libraryCopy = new ArrayList<>();
        libraryCopy.addAll(library); //makes a copy of the lib
        OrderByAuthor comparator = new OrderByAuthor();

        sort(libraryCopy, comparator); //pass the orderByAuthor comparotor

        return libraryCopy;
    }

    /**
     * Returns the list of library books whose due date is older than the input
     * date. The list is sorted by date (oldest first).
     * <p>
     * If no library books are overdue, returns an empty list.
     * The method returns 0 if the time represented by the argument is equal to the time represented by this Calendar object;
     * or a value less than 0 if the time of this Calendar is before the time represented by the argument;
     * or a value greater than 0 if the time of this Calendar is after the time represented.
     *
     */
    public ArrayList<LibraryBookGeneric<Type>> getOverdueList(int month, int day, int year) {
        ArrayList<LibraryBookGeneric<Type>> overDueList = new ArrayList<>();
        GregorianCalendar overDueDate = new GregorianCalendar(year, month, day);
        OrderByDueDate comparable = new OrderByDueDate();

        for(LibraryBookGeneric book : library){ //add books to overDue date
            if(book.getDueDate() != null){
                if (book.getDueDate().compareTo(overDueDate) > 0){ //if returns 1 book is overdue //TODO:Is this less than???
                    overDueList.add(book); //add it to the list
                }
            }
        }
        sort(overDueList,comparable); //sort it
        return overDueList; //return sorted list

    }


    /**
     * Performs a SELECTION SORT on the input ArrayList.
     * 1. Find the smallest item in the list.
     * 2. Swap the smallest item with the first item in the list.
     * 3. Now let the list be the remaining unsorted portion
     * (second item to Nth item) and repeat steps 1, 2, and 3.
     * compare returns -1, 0, 1
     */

    private static <ListType> void sort(ArrayList<ListType> list, Comparator<ListType> c) {
        for (int i = 0; i < list.size() - 1; i++) {
            int j, minIndex;
            for (j = i + 1, minIndex = i; j < list.size(); j++)
                if (c.compare(list.get(j), list.get(minIndex)) < 0)
                    minIndex = j;
            ListType temp = list.get(i);
            list.set(i, list.get(minIndex));
            list.set(minIndex, temp);
        }
    }


    /**
     * Comparator that defines an ordering among library books using the ISBN.
     */
    protected class OrderByIsbn implements Comparator<LibraryBookGeneric<Type>> {
        /**
         * Returns a negative value if lhs is smaller than rhs. Returns a positive
         * value if lhs is larger than rhs. Returns 0 if lhs and rhs are equal.
         */
        public int compare(LibraryBookGeneric<Type> lhs, LibraryBookGeneric<Type> rhs) {
            return (int) (lhs.getIsbn() - rhs.getIsbn());
        }
    }

    //Comparable - defines natural ordering, Comparator- we define how things are orderded

    /**
     * Comparator that defines an ordering among library books using the author,  and book title as a tie-breaker.
     * compare by author name, if name is the same compare book title
     */
    protected class OrderByAuthor implements Comparator<LibraryBookGeneric<Type>> {
        @Override
        public int compare(LibraryBookGeneric<Type> o1, LibraryBookGeneric<Type> o2) {
            if (o1.getAuthor().equals(o2.getAuthor())) { //if same Author, compare titles
                return o1.getTitle().compareTo(o2.getTitle());
            } else {
                return o1.getAuthor().compareTo(o2.getAuthor()); //returns 0 if they equal, 1 greater, -1 if less
            }
        }
    }

    /**
     * Comparator that defines an ordering among library books using the due date.
     * Returns -1 if 01 < 02, returns 0 if o1 == o2, returns 1 if o1 > o2
     * returns value of compare to... if lhs due date is greater than rhs return 1, if less than, -1 if equal 0
     */
    protected class OrderByDueDate implements Comparator<LibraryBookGeneric<Type>> {
        @Override
        public int compare(LibraryBookGeneric<Type> o1, LibraryBookGeneric<Type> o2) {
            return o1.getDueDate().compareTo(o2.getDueDate());
        }
    }

}
