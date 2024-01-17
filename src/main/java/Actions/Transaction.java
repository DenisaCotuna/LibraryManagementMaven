package Actions;

import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;
import Items.Book;
import Items.Borrowable;
import Items.Buyable;
import Items.Item;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private Integer ID;

    private Map<Item, Map<Boolean,Integer>> itemList = new HashMap<>();

    private double total;

    public Transaction(Integer ID) {
        this.ID = ID;
    }

    public double getTotal() {
        return total;
    }

    public Map<Item, Map<Boolean, Integer>> getItemList() {
        return itemList;
    }

    public void addItem(Item I, Boolean borrow, Integer NoCopies)
    {
        for(Map.Entry<Item, Map<Boolean, Integer>> set : itemList.entrySet())
        {
            if(set.getKey().getID().equals(I.getID())) {
                set.getValue().put(borrow, NoCopies);
                return;
            }
        }
        Map<Boolean,Integer> itemMap = new HashMap<>();
        itemMap.put(borrow,NoCopies);
        itemList.put(I,itemMap);
    }


    public void doTotal() throws NotEnoughCopiesException, NegativeNumberException {
        for(Map.Entry<Item, Map<Boolean, Integer>> set : itemList.entrySet()){
            Map<Boolean,Integer> itemMap = set.getValue();
            for(Map.Entry<Boolean,Integer> set2 : itemMap.entrySet()) {
                if (set2.getKey() && set.getKey() instanceof Book) {
                    total += ((Book) set.getKey()).getBorrowPrice() * set2.getValue();
                    ((Book) set.getKey()).borrowItem(set2.getValue());
                } else {
                    total += set.getKey().price * set2.getValue();
                    ((Buyable) set.getKey()).buyItem(set2.getValue());
                }
            }
        }
    }
}
