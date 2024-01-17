package Items;

import Exceptions.NegativeNumberException;
import Exceptions.NotEnoughCopiesException;

public interface Borrowable {
    public void borrowItem(Integer NoCopies) throws NotEnoughCopiesException, NegativeNumberException;
    public void returnItem(Integer NoCopies) throws NegativeNumberException;
}
