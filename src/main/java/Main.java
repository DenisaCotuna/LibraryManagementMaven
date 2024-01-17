import Application.Application;
import Exceptions.InvalidItemTypeException;
import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;
import io.InputDevice;
import io.OutputDevice;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, NegativeNumberException, NotEnoughCopiesException, InvalidItemTypeException {
        InputDevice id = new InputDevice(System.in);
        OutputDevice od = new OutputDevice(System.out);
        Application app = new Application(id,od);

        DatabaseCommands db = new DatabaseCommands();

        db.createNewTable();
        System.out.println("Adding album...");
        db.insert("A13",5);
        db.printAllContent();

        System.out.println("Adding book...");
        db.insert("B123",10);
        db.printAllContent();

        System.out.println("Correcting mistake");
        db.update(1,"A321",10);
        db.printAllContent();

        System.out.println("Deleting book");
        db.delete(1);
        db.printAllContent();

        System.out.println("Resetting the database...");
        db.executeCustomQuery("DELETE from Inventory;");

        if(args.length == 0)  app.run();
        else{
            for (int i = 0; i < args.length; i++)
            {
                if(args[i].equals("1")){
                    app.addItemsFromFIle(args[i+1]);
                    i++;
                }else if(args[i].equals("2")){
                    app.transactionFromFile(args[i+1]);
                    i++;
                }
                else if(args[i].equals("3")){
                    app.printInv();
                }
            }
        }
    }
}