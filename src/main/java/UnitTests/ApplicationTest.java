package UnitTests;

import Actions.Inventory;
import Actions.Transaction;
import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;
import Items.Album;
import Items.Book;
import io.InputDevice;
import io.OutputDevice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    @Test
    @DisplayName("Should create an album and test getters")
    void AlbumTestConstructorAndGetters() {
        Album album = new Album("A123",100.0,"5-Star","Stray Kids",5);
        assertEquals(album.getID(),"A123");
        assertEquals(album.getPrice(),100.0);
        assertEquals(album.getTitle(),"5-Star");
        assertEquals(album.getArtist(),"Stray Kids");
        assertEquals(album.getCopies(),5);
    }

    @Test
    @DisplayName("Should test album functionalities")
    void AlbumFunctionalities() throws NotEnoughCopiesException, NegativeNumberException {
        Album album = new Album("A123",100.0,"5-Star","Stray Kids",5);
        album.addCopies(10);
        assertThrows(NegativeNumberException.class, () -> album.addCopies(0),"The number you gave for the copies is <= 0");
        assertEquals(album.getCopies(),15);
        album.buyItem(5);
        assertEquals(album.getCopies(),10);
        assertThrows(NegativeNumberException.class, () -> album.buyItem(0),"The number you gave for the copies is <= 0");
        assertThrows(NotEnoughCopiesException.class, () -> album.buyItem(50),"Not enough copies in stock");
    }

    @Test
    @DisplayName("Should create a book and test getters")
    void BookTestConstructorAndGetters(){
        Book book = new Book("B123",50.0,"Amintiri din copilarie","Ion Creanga",5);
        assertEquals(book.getID(),"B123");
        assertEquals(book.getPrice(),50.0);
        assertEquals(book.getTitle(),"Amintiri din copilarie");
        assertEquals(book.getAuthor(),"Ion Creanga");
        assertEquals(book.getCopies(),5);
        assertEquals(book.getBorrowPrice(),10.0);
        assertEquals(book.getBorrowed(),0);
    }

    @Test
    @DisplayName("Should test book functionalities")
    void BookFunctionalities() throws NotEnoughCopiesException, NegativeNumberException {
        Book book = new Book("B123",50.0,"Amintiri din copilarie","Ion Creanga",5);
        book.addCopies(10);
        assertThrows(NegativeNumberException.class, () -> book.addCopies(0),"The number you gave for the copies is <= 0");
        assertEquals(book.getCopies(),15);
        book.buyItem(5);
        assertEquals(book.getCopies(),10);
        assertThrows(NegativeNumberException.class, () -> book.buyItem(0),"The number you gave for the copies is <= 0");
        assertThrows(NotEnoughCopiesException.class, () -> book.buyItem(50),"Not enough copies in stock");
        book.borrowItem(2);
        assertEquals(book.getCopies(),8);
        assertEquals(book.getBorrowed(),2);
        assertThrows(NegativeNumberException.class, () -> book.borrowItem(0),"The number you gave for the copies is <= 0");
        book.returnItem(1);
        assertEquals(book.getCopies(),9);
        assertEquals(book.getBorrowed(),1);
        assertThrows(NegativeNumberException.class, () -> book.returnItem(0),"The number you gave for the copies is <= 0");
    }

    @Test
    @DisplayName("Should create an inventory with no items and test getters.")
    void InventoryConstructorAndGetters(){
        Inventory inventory = new Inventory();
        assertEquals(inventory.getCash(),0);
        assertEquals(inventory.getAlbums().size(),0);
        assertEquals(inventory.getBooks().size(),0);
    }

    @Test
    @DisplayName("Should test inventory functionalities.")
    void InventoryFunctionalities() throws NegativeNumberException {
        Inventory inventory = new Inventory();
        inventory.addBook("B123",50.0,"Amintiri din copilarie","Ion Creanga",5);
        inventory.addAlbum("A123",100.0,"5-Star","Stray Kids",5);
        assertEquals(inventory.getBooks().size(),1);
        assertEquals(inventory.getAlbums().size(),1);
        inventory.addBook("B123",50.0,"Amintiri din copilarie","Ion Creanga",5);
        assertEquals(inventory.getBooks().get(0).getCopies(),10);
        inventory.addAlbum("A123",100.0,"5-Star","Stray Kids",5);
        assertEquals(inventory.getAlbums().get(0).getCopies(),10);
    }

    @Test
    @DisplayName("Should create a transaction with no items")
    void TransactionConstructorAndGetters(){
        Transaction transaction = new Transaction(1);
        assertEquals(transaction.getItemList().size(),0);
        assertEquals(transaction.getTotal(),0);
    }

    @Test
    @DisplayName("Should test transaction functionalities")
    void TransactionFunctionalities() throws NegativeNumberException, NotEnoughCopiesException {
        Transaction transaction = new Transaction(1);
        Album album = new Album("A123",100.0,"5-Star","Stray Kids",5);
        Book book = new Book("B123",50.0,"Amintiri din copilarie","Ion Creanga",5);
        transaction.addItem(book,true,2);
        transaction.addItem(book,false,1);
        transaction.addItem(album,false,3);
        assertEquals(transaction.getItemList().size(),2);
        transaction.doTotal();
        assertEquals(transaction.getTotal(),370.0);
    }

    @Test
    @DisplayName("Should create Input Device")
    void InputDevice(){
        InputDevice id = new InputDevice(System.in);
        assertEquals(id.getInputStream(),System.in);
    }

    @Test
    @DisplayName("Should create Output Device")
    void OutputDevice(){
        OutputDevice od = new OutputDevice(System.out);
        assertEquals(od.getOutputStream(),System.out);
    }

}