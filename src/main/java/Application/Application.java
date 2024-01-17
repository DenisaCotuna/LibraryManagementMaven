package Application;

import Actions.Inventory;
import Actions.Transaction;
import Exceptions.InvalidItemTypeException;
import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;
import Items.Album;
import Items.Book;
import Items.Item;
import io.InputDevice;
import io.OutputDevice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
    private Inventory inventory =  new Inventory();
    private List<Transaction> transactionList = new ArrayList<>();
    private InputDevice id;
    private OutputDevice od;
    private Integer noTransaction;


    public Application(InputDevice id, OutputDevice od) {
        this.id = id;
        this.od = od;
        noTransaction = 0;
    }

    public InputDevice getId() {
        return id;
    }

    public OutputDevice getOd() {
        return od;
    }

    public Integer getNoTransaction() {
        return noTransaction;
    }

    public void printInv(){
        for(Book b:inventory.getBooks())
        {
            String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + " number of borrowed copies: "+ b.getBorrowed();
            System.out.println(content);
        }
        for(Album a:inventory.getAlbums())
        {
            String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies();
            System.out.println(content);
        }
        System.out.println("");
    }

    public void addItemsFromFIle(String filename) throws IOException, NegativeNumberException, InvalidItemTypeException{
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String itemtype;
            while ((itemtype = reader.readLine()) != null)
            {
                if(!(itemtype.equals("Book")|| itemtype.equals("book") || itemtype.equals("Album") || itemtype.equals("album"))) {
                    throw new InvalidItemTypeException("Invalid item type. Item type can only be Book or Album");
                }
                String iditem = reader.readLine();
                try {
                    double price = Double.parseDouble(reader.readLine());
                    String title = reader.readLine();
                    String author = reader.readLine();
                    Integer NoCopies = Integer.parseInt(reader.readLine());
                    if(price <= 0.0) throw new NegativeNumberException("Price is <= 0.0");
                    if(NoCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                    if(itemtype.equals("Book")) inventory.addBook(iditem,price,title,author,NoCopies);
                    else inventory.addAlbum(iditem,price,title,author,NoCopies);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
            Files.newBufferedWriter(Path.of("inventory.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
            for(Book b:inventory.getBooks())
            {
                String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + "\n";
                od.writetoFile(content,"inventory.txt",true);
            }
            for(Album a:inventory.getAlbums())
            {
                String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies() + "\n";
                od.writetoFile(content,"inventory.txt",true);
            }
        }catch (FileNotFoundException e)
        {
            System.err.println("Could not find/open file. Please try again.");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void transactionFromFile(String filename)throws IOException, NegativeNumberException,NotEnoughCopiesException, InvalidItemTypeException{
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            noTransaction++;
            Transaction newTransaction = new Transaction(noTransaction);
            String itemtype;
            while ((itemtype = reader.readLine()) != null){
                if(!(itemtype.equals("Book")|| itemtype.equals("book") || itemtype.equals("Album") || itemtype.equals("album"))) {
                    throw new InvalidItemTypeException("Invalid item type. Item type can only be Book or Album");
                }
                String ID = reader.readLine();
                boolean found = false;
                if(itemtype.equals("Book")||itemtype.equals("book")){
                    for(Book b: inventory.getBooks()){
                        if(b.getID().equals(ID)){
                            found = true;
                            if(b.getCopies() == 0){
                                System.out.println("There is no copies in stock of the item with ID = "+ ID);
                                break;
                            }
                            String borrow = reader.readLine();
                            Integer noCopies = null;
                            try {
                                noCopies = Integer.parseInt(reader.readLine());
                                if(noCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                            }catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (b.getCopies() - noCopies < 0) throw new NotEnoughCopiesException("Not enough copies in stock");
                            if(borrow.equals("borrow"))
                                newTransaction.addItem(b,true, noCopies);
                            else if(borrow.equals("buy")) newTransaction.addItem(b,false, noCopies);
                            else System.out.println("Couldn't add item(s) with ID = "+ID);
                        }
                    }
                    if(!found) System.out.println("Sorry the book with ID = " + ID + " doesn't exist in the inventory");
                }
                else{
                    for (Album a: inventory.getAlbums()){
                        if(a.getID().equals(ID)){
                            found = true;
                            if (a.getCopies() == 0)
                            {
                                System.out.println("There is no copies in stock of this item with ID = " + ID);
                                break;
                            }
                            Integer noCopies = null;
                            try {
                                noCopies = Integer.parseInt(reader.readLine());
                                if(noCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                            }catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if(a.getCopies() - noCopies < 0) throw new NotEnoughCopiesException("Not enough copies in stock");
                            newTransaction.addItem(a, false, noCopies);
                        }
                    }
                }
            }
            Files.newBufferedWriter(Path.of("currentTransaction.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
            String contentNo = "Transaction number: " + noTransaction + "\n";
            od.writetoFile(contentNo,"currentTransaction.txt",true);
            for(Map.Entry<Item, Map<Boolean, Integer>> set : newTransaction.getItemList().entrySet())
            {
                Map<Boolean,Integer> itemMap = set.getValue();
                for (Map.Entry<Boolean,Integer> set2 : itemMap.entrySet()){
                    if(set.getKey() instanceof Book){
                        if(set2.getKey()){
                            String content = "Book ID = " + set.getKey().getID() + " borrow " + set2.getValue() + " x " + ((Book) set.getKey()).getBorrowPrice() + "\n";
                            od.writetoFile(content,"currentTransaction.txt",true);
                        }
                        else {
                            String content = "Book ID = " + set.getKey().getID() + " buy " + set2.getValue() + " x " + ((Book) set.getKey()).getPrice() + "\n";
                            od.writetoFile(content,"currentTransaction.txt",true);
                        }
                    }
                    else {
                        String content = "Album ID = " + set.getKey().getID() + " buy " + set2.getValue() + " x " + ((Album) set.getKey()).getPrice() + "\n";
                        od.writetoFile(content,"currentTransaction.txt",true);
                    }
                }
            }
            newTransaction.doTotal();
            String content1 = "The total of the transaction is " + newTransaction.getTotal();
            od.writetoFile(content1,"currentTransaction.txt", true);
            transactionList.add(newTransaction);
            Files.newBufferedWriter(Path.of("inventory.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
            for(Book b:inventory.getBooks())
            {
                String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + " number of borrowed copies = " + b.getBorrowed() + "\n";
                od.writetoFile(content,"inventory.txt",true);
            }
            for(Album a:inventory.getAlbums())
            {
                String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies() + "\n";
                od.writetoFile(content,"inventory.txt",true);
            }
        }catch (FileNotFoundException e)
        {
            System.err.println("Could not find/open file. Please try again.");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run() throws IOException, NegativeNumberException, NotEnoughCopiesException, InvalidItemTypeException {
        while (true)
        {
            System.out.println("Hello! What would you like to do?");
            System.out.println("Type 1 if you want to add one or more items to inventory.");
            System.out.println("Type 2 if you want to make one or more transactions.");
            System.out.println("Type 3 if you want to see the items in the inventory");
            System.out.println("Type 4 if you want to search for a specific item");
            System.out.println("If you want to exit, type exit");
            String cmd = id.read();
            if(cmd.equals("exit"))
            {
                System.exit(0);
            }else if(cmd.equals("1"))
            {
                while (true){
                    System.out.println("Would you like to add items from a file or by hand? Write file or hand. \nWrite done if you are done adding input.");
                    String answer = id.read();
                    if(answer.equals("file"))
                    {
                        System.out.println("Type the name of the file you wish to add input from.");
                        String filename = id.read();
                        addItemsFromFIle(filename);
                    }
                    else if(answer.equals("hand")) {
                        String cont = null;
                        do {
                            System.out.println("Is the item a book or an album?");
                            String itemtype = id.read();
                            if (itemtype.equals("book")) {
                                System.out.println("Type ID of the book");
                                String idbook = id.read();
                                boolean isinInv = false;
                                double price = 0.00;
                                String title = null;
                                String author = null;
                                int NoCopies = 0;
                                for (Book b : inventory.getBooks())
                                    if (b.getID().equals(idbook)) {
                                        isinInv = true;
                                        price = b.getPrice();
                                        title = b.getTitle();
                                        author = b.getAuthor();
                                        break;
                                    }
                                try {
                                    if (isinInv) {
                                        System.out.println("The book already exists in the inventory. \nAdd the number of copies you would like to add.");
                                        String noCopies = id.read();
                                        NoCopies = Integer.parseInt(noCopies);
                                        if(NoCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                                        inventory.addBook(idbook, price, title, author, NoCopies);
                                    } else {
                                        System.out.println("The book is not in the inventory. You will need to give extra information \n What's the price of the book?");
                                        price = Double.parseDouble(id.read());
                                        if(price <= 0.0) throw new NegativeNumberException("Price is <= 0.0");
                                        System.out.println("What's the title of the book?");
                                        title = id.read();
                                        System.out.println("What's the author of the book?");
                                        author = id.read();
                                        System.out.println("How many copies of the book would you like to add?");
                                        NoCopies = Integer.parseInt(id.read());
                                        if(NoCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                                        inventory.addBook(idbook,price,title,author,NoCopies);
                                    }
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }

                                System.out.println("Type continue if you would like to continue adding items. Type anything otherwise.");
                                cont = id.read();
                                Files.newBufferedWriter(Path.of("inventory.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
                                for(Book b:inventory.getBooks())
                                {
                                    String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + " number of borrowed copies = " + b.getBorrowed() + "\n";
                                    od.writetoFile(content,"inventory.txt",true);
                                }
                                for(Album a:inventory.getAlbums())
                                {
                                    String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies() + "\n";
                                    od.writetoFile(content,"inventory.txt",true);
                                }
                            } else if (itemtype.equals("album")) {
                                System.out.println("Type ID of the album");
                                String idalbum = id.read();
                                boolean isinInv = false;
                                double price = 0.00;
                                String title = null;
                                String artist = null;
                                int NoCopies = 0;
                                for (Album a : inventory.getAlbums())
                                    if (a.getID().equals(idalbum)) {
                                        isinInv = true;
                                        price = a.getPrice();
                                        title = a.getTitle();
                                        artist = a.getArtist();
                                        break;
                                    }
                                try {
                                    if (isinInv) {
                                        System.out.println("The album already exists in the inventory. \nAdd the number of copies you would like to add.");
                                        String noCopies = id.read();
                                        NoCopies = Integer.parseInt(noCopies);
                                        if(NoCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                                        inventory.addAlbum(idalbum, price, title, artist, NoCopies);
                                    } else {
                                        System.out.println("The album is not in the inventory. You will need to give extra information \n What's the price of the album?");
                                        price = Double.parseDouble(id.read());
                                        if(price <= 0.0) throw new NegativeNumberException("Price is <= 0.0");
                                        System.out.println("What's the title of the album?");
                                        title = id.read();
                                        System.out.println("What's the artist of the album?");
                                        artist = id.read();
                                        System.out.println("How many copies of the book would you like to add?");
                                        NoCopies = Integer.parseInt(id.read());
                                        if(NoCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                                        inventory.addAlbum(idalbum,price,title,artist,NoCopies);
                                    }
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }
                                System.out.println("Type continue if you would like to continue adding items. Type done if you are done adding items.");
                                cont = id.read();
                                Files.newBufferedWriter(Path.of("inventory.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
                                for(Book b:inventory.getBooks())
                                {
                                    String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + " number of borrowed copies = " + b.getBorrowed() + "\n";
                                    od.writetoFile(content,"inventory.txt",true);
                                }
                                for(Album a:inventory.getAlbums())
                                {
                                    String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies() + "\n";
                                    od.writetoFile(content,"inventory.txt",true);
                                }
                            } else{
                                System.out.println("Invalid. Type book or album");
                                cont = "continue";
                            }
                        } while (cont.equals("continue"));
                    }
                    else if(answer.equals("done")) break;
                    else System.out.println("Write file, hand or done.");
                }
            }
            else if(cmd.equals("2")){
                while (true)
                {
                    System.out.println("Would you like to make a transaction from a file or by hand? \nWrite done if you are done making transactions.");
                    String answer = id.read();
                    if(answer.equals("file")){
                        System.out.println("Type the name of the file you wish to add input from.");
                        String filename = id.read();
                        transactionFromFile(filename);
                    }
                    else if(answer.equals("hand")){
                        String newT = null;
                        do{
                            noTransaction ++;
                            Transaction newTransaction = new Transaction(noTransaction);
                            System.out.println("Transaction number: "+ noTransaction);
                            System.out.println("Is the item you want to add to the transaction a book or an album?");
                            String itemtype = id.read();
                            do{
                                if(itemtype.equals("book") || itemtype.equals("Book")){
                                    System.out.println("Type the ID of the item.");
                                    String ID = id.read();
                                    boolean found = false;
                                    for(Book b:inventory.getBooks()){
                                        if(b.getID().equals(ID)) {
                                            found = true;
                                            if (b.getCopies() == 0) {
                                                System.out.println("There is no copies in stock of this item.");
                                                break;
                                            }
                                            System.out.println("There is " + b.getCopies() + " copies of this book in stock. \n Does the customer want to borrow the book? Type yes or no.");
                                            String borrow = id.read();
                                            System.out.println("Type the number of copies the customer wants to buy/borrow");
                                            Integer noCopies = null;
                                            try {
                                                noCopies = Integer.parseInt(id.read());
                                                if(noCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                            if (b.getCopies() - noCopies < 0) throw new NotEnoughCopiesException("Not enough copies in stock");
                                            do{
                                                if (borrow.equals("yes")){
                                                    newTransaction.addItem(b, true, noCopies);
                                                    break;
                                                }
                                                else if(borrow.equals("no")) {
                                                    newTransaction.addItem(b, false, noCopies);
                                                    break;
                                                }
                                                else System.out.println("Please type yes or no");
                                            }while (true);
                                        }
                                    }
                                    if(!found) System.out.println("Sorry, the book doesn't exist in the inventory");
                                }
                                else if(itemtype.equals("album") || itemtype.equals("Album")){
                                    System.out.println("Type the ID of the item.");
                                    String ID = id.read();
                                    boolean found = false;
                                    for (Album a:inventory.getAlbums()){
                                        if(a.getID().equals(ID)){
                                            found = true;
                                            if (a.getCopies() == 0)
                                            {
                                                System.out.println("There is no copies in stock of this item.");
                                                break;
                                            }
                                            System.out.println("There is " + a.getCopies() + " copies of this album in stock.");
                                            System.out.println("Type the number of albums the customer wants to buy.");
                                            Integer noCopies = null;
                                            try {
                                                noCopies = Integer.parseInt(id.read());
                                                if(noCopies <= 0) throw new NegativeNumberException("Number of copies <= 0");
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                            if (a.getCopies() - noCopies < 0) throw new NotEnoughCopiesException("Not enough copies in stock");
                                            newTransaction.addItem(a, false, noCopies);
                                        }
                                    }
                                }
                                else throw new InvalidItemTypeException("Invalid item type. Item type can only be Book or Album");
                                System.out.println("If you want to get the total, type total. \nIf you want to continue adding items, is the item you want to add to the transaction a book or an album?");
                                itemtype = id.read();
                            }while (!itemtype.equals("total"));
                            Files.newBufferedWriter(Path.of("currentTransaction.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
                            String contentNo = "Transaction number: " + noTransaction + "\n";
                            od.writetoFile(contentNo,"currentTransaction.txt",true);
                            for(Map.Entry<Item, Map<Boolean, Integer>> set : newTransaction.getItemList().entrySet())
                            {
                                Map<Boolean,Integer> itemMap = set.getValue();
                                for (Map.Entry<Boolean,Integer> set2 : itemMap.entrySet()){
                                    if(set.getKey() instanceof Book){
                                        if(set2.getKey()){
                                            String content = "Book ID = " + set.getKey().getID() + " borrow " + set2.getValue() + " x " + ((Book) set.getKey()).getBorrowPrice() + "\n";
                                            od.writetoFile(content,"currentTransaction.txt",true);
                                        }
                                        else {
                                            String content = "Book ID = " + set.getKey().getID() + " buy " + set2.getValue() + " x " + ((Book) set.getKey()).getPrice() + "\n";
                                            od.writetoFile(content,"currentTransaction.txt",true);
                                        }
                                    }
                                    else {
                                        String content = "Album ID = " + set.getKey().getID() + " buy " + set2.getValue() + " x " + ((Album) set.getKey()).getPrice() + "\n";
                                        od.writetoFile(content,"currentTransaction.txt",true);
                                    }
                                }
                            }
                            newTransaction.doTotal();
                            String content1 = "The total of the transaction is " + newTransaction.getTotal();
                            od.writetoFile(content1,"currentTransaction.txt", true);
                            System.out.println("The total of the transaction is " + newTransaction.getTotal());
                            transactionList.add(newTransaction);
                            Files.newBufferedWriter(Path.of("inventory.txt"), new StandardOpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
                            for(Book b:inventory.getBooks())
                            {
                                String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + " number of borrowed copies = " + b.getBorrowed() + "\n";
                                od.writetoFile(content,"inventory.txt",true);
                            }
                            for(Album a:inventory.getAlbums())
                            {
                                String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies() + "\n";
                                od.writetoFile(content,"inventory.txt",true);
                            }
                            System.out.println("If you want to make a new transaction, type new, else type done.");
                            newT = id.read();
                        }while (newT.equals("new"));
                    }
                    else if(answer.equals("done")) break;
                    else System.out.println("Write file, hand or done.");
                }
            }
            else if(cmd.equals("3")){
                printInv();
            }
            else if(cmd.equals("4")){
                System.out.println("Is the item you are searching for a book or an album?");
                String itemtype = id.read();
                if(itemtype.equals("book")){
                    System.out.println("Enter title of the book:");
                    String title = id.read();
                    boolean found = false;
                    for(Book b:inventory.getBooks())
                    {
                        if(b.getTitle().equals(title)) {
                            found = true;
                            String content = "Book: ID = " + b.getID() + " price = " + b.getPrice() + " title = " + b.getTitle() + " author = " + b.getAuthor() + " number of copies = " + b.getCopies() + "\n";
                            System.out.print(content);
                        }
                    }
                    if(!found) System.out.println("Sorry, the book doesn't exist in the inventory");
                }
                else if(itemtype.equals("album")){
                    System.out.println("Enter title of the album:");
                    String title = id.read();
                    boolean found = false;
                    for(Album a:inventory.getAlbums())
                    {
                        if(a.getTitle().equals(title)) {
                            found = true;
                            String content = "Album: ID = " + a.getID() + " price = " + a.getPrice() + " title = " + a.getTitle() + " artist = " + a.getArtist() + " number of copies = " + a.getCopies() + "\n";
                            System.out.print(content);
                        }
                    }
                    if(!found) System.out.println("Sorry, the album doesn't exist in the inventory");
                }
                else System.out.println("Invalid. Type book or album");
            }
            else System.out.println("Invalid command.");
        }
    }


}


