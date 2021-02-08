package assignment02;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing class for LibraryGeneric.
 */
public class LibraryGenericTest {

    public static void main(String[] args) {

        // test a library that uses names (String) to id patrons
        LibraryGeneric<String> lib1 = new LibraryGeneric<String>();
        lib1.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib1.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib1.add(9780446580342L, "David Baldacci", "Simple Genius");
        lib1.add(9780446580379L, "David Baldacci", "Alfred is Hot(This comes before Simple Genius)");

        String patron1 = "Jane Doe";

        if (!lib1.checkout(9780330351690L, patron1, 1, 1, 2008))
            System.err.println("TEST FAILED: first checkout");
        if (!lib1.checkout(9780374292799L, patron1, 1, 1, 2008))
            System.err.println("TEST FAILED: second checkout");
        ArrayList<LibraryBookGeneric<String>> booksCheckedOut1 = lib1.lookup(patron1);
        if (booksCheckedOut1 == null || booksCheckedOut1.size() != 2
                || !booksCheckedOut1.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild"))
                || !booksCheckedOut1.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat"))
                || !booksCheckedOut1.get(0).getHolder().equals(patron1)
                || !booksCheckedOut1.get(0).getDueDate().equals(new GregorianCalendar(2008, 1, 1))
                || !booksCheckedOut1.get(1).getHolder().equals(patron1)
                || !booksCheckedOut1.get(1).getDueDate().equals(new GregorianCalendar(2008, 1, 1)))
            System.err.println("TEST FAILED: lookup(holder)");
        if (!lib1.checkin(patron1))
            System.err.println("TEST FAILED: checkin(holder)");

        // test a library that uses phone numbers (PhoneNumber) to id patrons
        LibraryGeneric<PhoneNumber> lib2 = new LibraryGeneric<PhoneNumber>();
        lib2.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib2.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib2.add(9780446580342L, "David Baldacci", "Simple Genius");
        lib2.add(9780446580300L, "Thomas L. Friedman", "Zoe's Zebra (Should come after The World is Flat)");

        PhoneNumber patron2 = new PhoneNumber("801.555.1234");

        if (!lib2.checkout(9780330351690L, patron2, 1, 1, 2008))
            System.err.println("TEST FAILED: first checkout");
        if (!lib2.checkout(9780374292799L, patron2, 1, 1, 2008))
            System.err.println("TEST FAILED: second checkout");
        ArrayList<LibraryBookGeneric<PhoneNumber>> booksCheckedOut2 = lib2.lookup(patron2);
        if (booksCheckedOut2 == null || booksCheckedOut2.size() != 2
                || !booksCheckedOut2.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild"))
                || !booksCheckedOut2.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat"))
                || !booksCheckedOut2.get(0).getHolder().equals(patron2)
                || !booksCheckedOut2.get(0).getDueDate().equals(new GregorianCalendar(2008, 1, 1))
                || !booksCheckedOut2.get(1).getHolder().equals(patron2)
                || !booksCheckedOut2.get(1).getDueDate().equals(new GregorianCalendar(2008, 1, 1)))
            System.err.println("TEST FAILED: lookup(holder)");


        if (lib2.lookup(9780330351690L) != patron2 || lib2.lookup(9780374292799L) != patron2)
            System.err.println("TEST FAILED-- lookup by phone number");


        if (!lib2.checkin(patron2))
            System.err.println("TEST FAILED: checkin(holder)");


        /**
         * Sorting using ISBN
         * Test on both libraries that use Strings for holders and Phone Numbers
         *
         */
        ArrayList<LibraryBookGeneric<PhoneNumber>> ISBNSorted2 = lib2.getInventoryList();  //Lib2
        for (int i = 0; i < ISBNSorted2.size() - 1; i++) {
            if (ISBNSorted2.get(i).getIsbn() > ISBNSorted2.get(i + 1).getIsbn()) //Should check all indices to see if sorted
                System.err.println("TEST FAILED -- ISBN sorted");
        }

        ArrayList<LibraryBookGeneric<String>> ISBNSorted1 = lib1.getInventoryList(); //Lib1
        for (int i = 0; i < ISBNSorted1.size() - 1; i++) {
            if (ISBNSorted1.get(i).getIsbn() > ISBNSorted1.get(i + 1).getIsbn()) //Should check all indices to see if sorted
                System.err.println("TEST FAILED -- ISBN sorted");
        }


        /**
         * Testing for Sorting based on Author
         * If Author is the same, sort based on title
         * Test using both lib that uses names and lib that uses phone numbers
         * Test mid sized lib, verify that all names are sorted
         */

        LibraryGeneric authorLib1 = new LibraryGeneric(); //make a new library to use in tests
        authorLib1.addAll("Mushroom_Publishing.txt"); //add books to lib.

        //Test Author sorting
        ArrayList<LibraryBookGeneric<String>> libAuthorSorted1 = lib1.getOrderedByAuthor();
        for (int i = 0; i < libAuthorSorted1.size() - 1; i++) {
            if (libAuthorSorted1.get(i).getAuthor().compareTo(libAuthorSorted1.get(i + 1).getAuthor()) > 0) {
                System.err.println("TEST FAILED-- Author sorting out of place(Lib1)");
            }
        }
        ArrayList<LibraryBookGeneric<PhoneNumber>> libAuthorSorted2 = lib2.getOrderedByAuthor();
        for (int i = 0; i < libAuthorSorted2.size() - 1; i++) {
            if (libAuthorSorted2.get(i).getAuthor().compareTo(libAuthorSorted2.get(i + 1).getAuthor()) > 0) {
                System.err.println("TEST FAILED-- Author sorting out of place(Lib2)");
            }
        }

        //bigger lib, return entire lib sorted
        ArrayList<LibraryBookGeneric<String>> wholeLibAuthorSorted = authorLib1.getOrderedByAuthor(); //get everything sorted by author
        System.out.println("Mid lib size " + wholeLibAuthorSorted.size());
        for (int i = 0; i < wholeLibAuthorSorted.size() - 1; i++) {
            if (wholeLibAuthorSorted.get(i).getAuthor().compareTo(wholeLibAuthorSorted.get(i + 1).getAuthor()) > 0) {
                System.err.println("TEST FAILED -- Author sorted out of place");
                assertEquals("Alan Burt Akers", wholeLibAuthorSorted.get(0));
            }
        }


        /**
         * Sort by Due Date
         * Created new lib for testing purpose
         * verified things were sorted in correct order
         * Checkout multiple books and verify the overdue book are added to overdue list
         */
        String patronShan = "Shannon McCallum";

        LibraryGeneric dueDateTestSortingLib = new LibraryGeneric(); //make a new library to use in tests
        dueDateTestSortingLib.addAll("Mushroom_Publishing.txt");//populate it

        //Checkout a few books with different due dates
        //last 4 titles == Operation: Sergeant York, The Lilly and the Bull, The anxiety relief Program, Transit to Scorpio
        dueDateTestSortingLib.checkout(9781843192039L, patronShan, 4, 10, 2020); //operation Srg York
        dueDateTestSortingLib.checkout(9781843192701L, patronShan, 9, 10, 2020);// The Lilly and the Bull
        dueDateTestSortingLib.checkout(9781843192954L, patronShan, 7, 10, 2020); //Anxiety Relief program
        dueDateTestSortingLib.checkout(9781843193319L, patronShan, 1, 10, 2020); //Transit to scorpio
        //SORTING ORDER: Transit to scorpio, Sgt York, Anxiety relief, The lily and bull

        //Checkout books that are not due.  12/25/2020 these books should not be in overDueList
        dueDateTestSortingLib.checkout(9781843190677L, patronShan, 12, 25, 2020); //Herbs for healthy skin
        dueDateTestSortingLib.checkout(9781843190516L, patronShan, 12, 25, 2020); // The Fuehrermaster


        //test lookup by string
        if (dueDateTestSortingLib.lookup(9781843192039L) != patronShan
                || dueDateTestSortingLib.lookup(9781843192701L) != patronShan
                || dueDateTestSortingLib.lookup(9781843192954L) != patronShan) {
            System.err.println("TEST FAILED -- Lookup generic (mid lib))");
        }


        ArrayList<LibraryBookGeneric<String>> overDueBooks1 = dueDateTestSortingLib.getOverdueList(11, 8, 2020);
        int bookCount = 0;
        for (LibraryBookGeneric book : overDueBooks1) {
            // System.out.println("Author: " + book.getAuthor() + " Title " + book.getTitle() + " ISBN: " + book.getIsbn());
            bookCount++;
        }
        System.out.println("OverDue book count: " + bookCount);

        for (int i = 0; i < overDueBooks1.size() - 1; i++) {
            if (overDueBooks1.get(i).getDueDate().compareTo(overDueBooks1.get(i + 1).getDueDate()) > 0) {
                System.err.println("TEST FAILED -- Due Date Sorting(Mid lib)");
            }
        }


        //shan returns books
        dueDateTestSortingLib.checkin(patronShan);

        /**
         * new patron checks out entire lib
         * due dates are put in decsending order,
         * from libbook[0] bing the latest due date and libbook[size - 1] being the earliest
         * get Author method should return book sorted in reversed order
         */

        String patronKirk = "Kirk Hietpas";
        int bookCountAfterReturn = 0;
        for (LibraryBookGeneric book : overDueBooks1) {
            //System.out.println("Author: " + book.getAuthor() + " Title " + book.getTitle() + " ISBN: " + book.getIsbn());
            bookCount++;
        }
        //System.out.println("OverDue book count after return, FINE HER: " + bookCountAfterReturn);
        /**
         * Checkout entire lib,
         * books should be sorted in reverse order when/ get overdue list
         * Also try to checkout books that are already checked out
         * Also try and checkout book that doesnt exist
         */


        //kirk checks out entire lib
        int day = 30;
        ArrayList<LibraryBookGeneric> ISBNList = dueDateTestSortingLib.getLibList();
        for (int i = 0; i < dueDateTestSortingLib.getLibSize(); i++) {
            dueDateTestSortingLib.checkout(ISBNList.get(i).getIsbn(), patronKirk, 10, day, 2020);
            day--;
        }



        //all going to be checked out, should be sorted in reverse order when checked
        ArrayList<LibraryBookGeneric<String>> kirkOverDueBooks = dueDateTestSortingLib.getOverdueList(9, 8, 2020);
        for (int i = 0; i < kirkOverDueBooks.size() - 1; i++) { //if returns > 0 lhs is greater than rhs
            if (kirkOverDueBooks.get(i).getDueDate().compareTo(kirkOverDueBooks.get(i + 1).getDueDate()) > 0) {
                System.err.println("TEST FAILED -- Author Sorting (Mid Lib)");

            }
        }
        for(int i = 0; i < 10; i++){ //should return false if tyring to checkout a book that has already been checked out
            assertEquals(false, dueDateTestSortingLib.checkout(ISBNList.get(i).getIsbn(), "Bob the Builder", 3, 3, 2020));
        }

        //checkout books that dont exist
        for(int i = 0; i < 10; i++){
            long randISBN = LibraryTest.generateIsbn();
            assertEquals(false, dueDateTestSortingLib.checkout(randISBN, "Kirkie",2,4,2000));
        }



        System.out.println("Testing done.");
    }
}
