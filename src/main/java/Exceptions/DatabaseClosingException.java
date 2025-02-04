package Exceptions;

public class DatabaseClosingException extends RuntimeException {
    public DatabaseClosingException(String message) {
        super(message);
    }
}
