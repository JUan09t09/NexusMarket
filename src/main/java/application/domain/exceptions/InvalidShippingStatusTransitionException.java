package application.domain.exceptions;

public class InvalidShippingStatusTransitionException extends DomainException {
    public InvalidShippingStatusTransitionException(String message) {
        super(message);
    }
}
