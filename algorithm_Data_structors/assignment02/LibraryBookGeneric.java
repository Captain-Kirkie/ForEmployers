package assignment02;


import java.util.GregorianCalendar;

public class LibraryBookGeneric <Type> extends Book { //Generic

    private GregorianCalendar dueDate; //specified due date
    private Type holder; //can be string name, int phone num, long ID etc...
    private Boolean checkedOut; //changes to true when book is checked out, false when in

    public LibraryBookGeneric(long isbn, String author, String title) {
        super(isbn, author, title);
        this.checkedOut = false;
    }

    //methods
    public Type getHolder() { return this.holder; } //returns bookHolder of whatever type

    public GregorianCalendar getDueDate() { return this.dueDate; } //returns specific gregorian calendar this.dueDate

    public boolean isCheckedOut(){ return checkedOut;} //returns true if this is checked out, false if its not

    /**
     *
     * @param holder of type specified by lib
     * @param day of the month
     * @param month month of the year
     * @param year whatever year it is
     * when checked out, holder associated with library book
     * Make new Gre                  gCalender and sets it with specified due date
     */

    public void checkBookOut(Type holder, int day, int month, int year){
        checkedOut = true;
        this.holder = holder;
        this.dueDate = new GregorianCalendar(year, month, day); //pass in due date
    }

    //Resets values to false/null
    public void checkBookIn(){
        checkedOut = false;
        this.holder = null;
        this.dueDate = null;
    }

}
