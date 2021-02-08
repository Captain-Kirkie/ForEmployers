package assignment02;

import java.util.GregorianCalendar;

public class LibraryBook extends Book {

    private GregorianCalendar dueDate;
    private String holder;
    private Boolean checkedOut; //changes to true when book is checked out, false when in

    public LibraryBook(long isbn, String author, String title) { //constructor
        super(isbn, author, title);
        checkedOut = false;
        holder = null;
        dueDate = null;
    }

    //methods
    public String getHolder() { return this.holder; }

    public GregorianCalendar getDueDate() { return this.dueDate; }

    public boolean isCheckedOut(){ return checkedOut;}//returns true if this is checked out, false if its not

    public void checkBookOut(String holder, int day, int month, int year){  //when checked out, holder associated with library book
        checkedOut = true;
        this.holder = holder;
        this.dueDate = new GregorianCalendar(year, month, day); //pass in due date
    }

    public void checkBookIn(){
        checkedOut = false;
        this.holder = null;
        this.dueDate = null;
    }


}
