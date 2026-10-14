package application.domain.exceptions;

public class OrderNotPhysicalException extends DomainException {
    public OrderNotPhysicalException(String message) {
        super(message);
    }
}
