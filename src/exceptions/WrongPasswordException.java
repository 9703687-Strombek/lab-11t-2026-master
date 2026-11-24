package exceptions;

// Неправильний пароль
public class WrongPasswordException extends AccountException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
