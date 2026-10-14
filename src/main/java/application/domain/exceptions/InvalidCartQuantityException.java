package application.domain.exceptions;

public class InvalidCartQuantityException extends DomainException {
    public InvalidCartQuantityException(String message) {
        super(message);
    }
}
