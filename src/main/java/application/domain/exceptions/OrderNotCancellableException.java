package application.domain.exceptions;

public class OrderNotCancellableException extends DomainException {
    public OrderNotCancellableException(String message) {
        super(message);
    }
}
