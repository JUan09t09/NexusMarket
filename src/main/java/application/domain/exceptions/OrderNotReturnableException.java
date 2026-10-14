package application.domain.exceptions;

public class OrderNotReturnableException extends DomainException {
    public OrderNotReturnableException(String message) {
        super(message);
    }
}
