package application.domain.exceptions;

public class InvalidMovementQuantityException extends DomainException {
    public InvalidMovementQuantityException(String message) {
        super(message);
    }
}
