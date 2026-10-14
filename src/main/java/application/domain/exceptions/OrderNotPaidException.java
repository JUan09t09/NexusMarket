package application.domain.exceptions;

public class OrderNotPaidException extends DomainException {
    public OrderNotPaidException(String message) {
        super(message);
    }
}
