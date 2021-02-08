package assignment02;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    Book book1, book2;


    @BeforeEach
    public void setUp() {
        book1 = new Book(9780399562709L, "Tommy Caldwell", "The Push");
        book2 = new Book(9781250313195L, "Tamsyn Muir", "Gideon the Ninth");
    }

    @AfterEach
    public void reset() {
        book1 = null;
    }

    @Test
    void getAuthor() {
        String author1 = book1.getAuthor();
        assertEquals("Tommy Caldwell", author1);
        assertEquals("Tamsyn Muir", book2.getAuthor());
    }

    @Test
    void getIsbn() {
        assertEquals(9780399562709L, book1.getIsbn());
        assertEquals(9781250313195L, book2.getIsbn());
    }

    @Test
    void getTitle() {
        assertEquals("The Push", book1.getTitle());
        assertEquals("Gideon the Ninth", book2.getTitle());
    }

    @Test
    void testEquals() {
        assertEquals(book1, book1);
        assertEquals(book2, book2);
        assertTrue(book1.equals(book1));
        assertEquals(false, book1.equals(book2));

    }

//    @Test
//    void testToString() {
//    }
//
//    @Test
//    void testHashCode() {
//    }
}