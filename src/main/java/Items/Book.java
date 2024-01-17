package Items;

import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;

public class Book extends Item implements Borrowable, Buyable {
    private String title;
    private String author;
    private Integer copies;
    private double borrowPrice;
    private Integer borrowed;

    public Book(String ID, double price, String title, String author, Integer NoCopies) {
        super(ID, price);
        this.title = title;
        this.author = author;
        this.copies = NoCopies;
        this.borrowPrice = (20.0/100.0) * price;
        this.borrowed = 0;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getCopies() {
        return copies;
    }

    public Integer getBorrowed() {
        return borrowed;
    }

    public double getBorrowPrice() {
        return borrowPrice;
    }

    public void addCopies(Integer NoCopies) throws NegativeNumberException {
        if(NoCopies <= 0) throw new NegativeNumberException("The number you gave for the copies is <= 0");
        this.copies += NoCopies;
    }

    public void removeCopies(Integer NoCopies) throws NotEnoughCopiesException, NegativeNumberException {
        if(NoCopies <= 0) throw new NegativeNumberException("The number you gave for the copies is <= 0");
        if(this.copies - NoCopies < 0 ) throw new NotEnoughCopiesException("Not enough copies in stock");
        this.copies -= NoCopies;
    }

    public void borrowBook(Integer NoCopies) throws NegativeNumberException {
        if(NoCopies <= 0) throw new NegativeNumberException("The number you gave for the copies is <= 0");
        this.borrowed += NoCopies;
    }

    public void returnBook(Integer NoCopies) throws NegativeNumberException {
        if(NoCopies <= 0) throw new NegativeNumberException("The number you gave for the copies is <= 0");
        this.borrowed -= NoCopies;
    }

    @Override
    public void buyItem(Integer NoCopies) throws NotEnoughCopiesException, NegativeNumberException {
        removeCopies(NoCopies);
    }

    @Override
    public void borrowItem(Integer NoCopies) throws NotEnoughCopiesException, NegativeNumberException {
        removeCopies(NoCopies);
        borrowBook(NoCopies);
    }

    @Override
    public void returnItem(Integer NoCopies) throws NegativeNumberException {
        returnBook(NoCopies);
        addCopies(NoCopies);
    }
}
