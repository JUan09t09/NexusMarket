package application.domain.exceptions;

public class InventoryNotAvailableException extends DomainException {
    public InventoryNotAvailableException(String message) {
        super(message);
    }
}
