package Items;

import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;

public interface Buyable{
    public void buyItem(Integer NoCopies) throws NotEnoughCopiesException, NegativeNumberException;
}
