package application.domain.exceptions;

public class InventoryNotFoundException extends DomainException {
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
