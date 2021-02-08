package assignment02;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Testing class for Library.
 */
public class LibraryTest {

    public static void main(String[] args) {
        // test an empty library
        Library lib = new Library();

        if (lib.lookup(978037429279L) != null)
            System.err.println("TEST FAILED -- empty library: lookup(isbn)");
        ArrayList<LibraryBook> booksCheckedOut = lib.lookup("Jane Doe");
        if (booksCheckedOut == null || booksCheckedOut.size() != 0)
            System.err.println("TEST FAILED -- empty library: lookup(holder)");
        if (lib.checkout(978037429279L, "Jane Doe", 1, 1, 2008))
            System.err.println("TEST FAILED -- empty library: checkout");
        if (lib.checkin(978037429279L))
            System.err.println("TEST FAILED -- empty library: checkin(isbn)");
        if (lib.checkin("Jane Doe"))
            System.err.println("TEST FAILED -- empty library: checkin(holder)"); //TODO: Failing

        // test a small library
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        if (lib.lookup(9780330351690L) != null)
            System.err.println("TEST FAILED -- small library: lookup(isbn)");
        if (!lib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008))
            System.err.println("TEST FAILED -- small library: checkout");
        booksCheckedOut = lib.lookup("Jane Doe");
        if (booksCheckedOut == null || booksCheckedOut.size() != 1
                || !booksCheckedOut.get(0).equals(new Book(9780330351690L, "Jon Krakauer", "Into the Wild"))
                || !booksCheckedOut.get(0).getHolder().equals("Jane Doe")
                || !booksCheckedOut.get(0).getDueDate().equals(new GregorianCalendar(2008, 1, 1)))
            System.err.println("TEST FAILED -- small library: lookup(holder)");
        if (!lib.checkin(9780330351690L))
            System.err.println("TEST FAILED -- small library: checkin(isbn)");
        if (lib.checkin("Jane Doe"))
            System.err.println("TEST FAILED -- small library: checkin(holder)");
        //check in jane does book
        lib.checkin("Jane Doe");
        ArrayList<LibraryBook> janesBooksCheckedIn = lib.lookup("Jane Doe");
        if (janesBooksCheckedIn.size() != 0)
            System.err.println("TEST FAILED -- Small lib: Checkin");

        // test a medium library
        lib.addAll("Mushroom_Publishing.txt"); //Add a bunch of books

        ArrayList<LibraryBook> testBookList = lib.getLibList(); //create a second list of books in lib for testing
        String testUser1 = "Shannon Mccallum";
        String testUser2 = "Kirk Hietpas";
        for (int i = 11; i <= 20; i++) { //shannon is going to checkout 10 books
            lib.checkout(testBookList.get(i).getIsbn(), testUser1, 3, 20, 2025);
        }
        ArrayList<LibraryBook> shansBooks = lib.lookup(testUser1);
        for (LibraryBook book : shansBooks) { // verify what shan checked out, make sure boolean is set
            // System.out.println("Shan checked out Title: " + book.getTitle() + "Author" + book.getAuthor());
            if (!book.isCheckedOut()|| book.getHolder() != testUser1){
                System.err.println("TEST FAILED -- mid library: checkout 10 books");
            } //if the books checked out bool is false
            if(!book.getDueDate().equals(new GregorianCalendar(2025, 3, 20))){
                System.err.println("TEST FAILED - Due Date wrong (mid lib)");
            }


        }
        if (shansBooks.size() != 10) //Size should be correct
            System.err.println("TEST FAILED -- mid library: books checked out incorrect");
        //Try and check in All of Shannon's book
        lib.checkin(testUser1);
        ArrayList<LibraryBook> shanEmptyList = lib.lookup(testUser1);
        if (shanEmptyList.size() != 0) {
            System.err.println("TEST FAILED -- Checkin Shans Books");
        }

        //check to see if a book that is already checked out can be checked out again
        lib.checkout(9781843192701L, testUser1, 3, 20, 1993);
        ArrayList<LibraryBook> singleCheckedOutBook = lib.lookup(testUser1);
        for (LibraryBook book : singleCheckedOutBook) {
            System.out.println("Shans book " + "Author " + book.getAuthor() + " Title " + book.getTitle() + " ISBN " + book.getIsbn());
        }
        //Try to check out
        //Author Moyra Caldecott Title The Lily and the Bull ISBN 9781843192701
        if (lib.checkout(9781843192701L, testUser2, 4, 5, 2288)) //should return fales if the book has been checked out.
            System.err.println("TEST Failed -- Re-CheckingOut a book ");
        if (singleCheckedOutBook.get(0).getHolder() != testUser1)
            System.err.println("TEST FAILED -- HolderNotSet");

        //if book is not in lib, return false
        long randISBN = generateIsbn(); //verify this is returning false
        if (lib.checkout(randISBN, testUser1, 3, 20, 2025))
            System.err.println("TEST FAILED -- CheckOut w/ nonExistant ISBN");
        if (!lib.checkout(testBookList.get((int) (Math.random() * 10)).getIsbn(), testUser1, 3, 20, 1993)) {
            System.err.println("TEST FAILED -- UserCheckout: Mid Lib");
        }


        System.out.println("Testing done.");
    }

    /**
     * Returns a library of "dummy" books (random ISBN and placeholders for author
     * and title).
     * <p>
     * Useful for collecting running times for operations on libraries of varying
     * size.
     *
     * @param size -- size of the library to be generated
     */
    public static ArrayList<LibraryBook> generateLibrary(int size) {
        ArrayList<LibraryBook> result = new ArrayList<LibraryBook>();

        for (int i = 0; i < size; i++) {
            // generate random ISBN
            Random randomNumGen = new Random();
            String isbn = "";
            for (int j = 0; j < 13; j++)
                isbn += randomNumGen.nextInt(10);

            result.add(new LibraryBook(Long.parseLong(isbn), "An author", "A title"));
        }

        return result;
    }

    /**
     * Returns a randomly-generated ISBN (a long with 13 digits).
     * <p>
     * Useful for collecting running times for operations on libraries of varying
     * size.
     */
    public static long generateIsbn() {
        Random randomNumGen = new Random();

        String isbn = "";
        for (int j = 0; j < 13; j++)
            isbn += randomNumGen.nextInt(10);

        return Long.parseLong(isbn);
    }
}
