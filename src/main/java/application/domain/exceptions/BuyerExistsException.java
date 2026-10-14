package application.domain.exceptions;

public class BuyerExistsException extends DomainException {
    public BuyerExistsException(String message) {
        super(message);
    }
}
