package application.domain.exceptions;

public class ShipmentNotFoundException extends DomainException {
    public ShipmentNotFoundException(String message) {
        super(message);
    }
}
