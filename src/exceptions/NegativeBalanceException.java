package exceptions;

//Не від'ємний баланс
public class NegativeBalanceException extends AccountException {
    public NegativeBalanceException(String message) {
        super(message);
    }
}
