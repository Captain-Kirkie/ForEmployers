package assignment02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryBookTest {
    LibraryBook book3, book4;

    @BeforeEach
    public void setUp() {
        book3 = new LibraryBook(9780399562709L, "Tommy Caldwell", "The Push");
        book4 = new LibraryBook(9781250313195L, "Tamsyn Muir", "Gideon the Ninth");
    }

    @Test
    void getHolder() {
        assertEquals(null, book3.getHolder());
    }

    @Test
    void getDueDate() {
    }

    @Test
    void isCheckedOut() {
    }

    @Test
    void checkBookOut() {
    }

    @Test
    void checkBookIn() {
    }
}