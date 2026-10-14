package application.domain.exceptions;

public class InvalidProductDataException extends DomainException {
    public InvalidProductDataException(String message) {
        super(message);
    }
}
