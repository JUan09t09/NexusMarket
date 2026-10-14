package application.domain.exceptions;

public class InvalidProductStatusTransitionException extends DomainException {
    public InvalidProductStatusTransitionException(String message) {
        super(message);
    }
}
