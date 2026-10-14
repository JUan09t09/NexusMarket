package application.domain.exceptions;

public class ShipmentAlreadyExistsException extends DomainException {
    public ShipmentAlreadyExistsException(String message) {
        super(message);
    }
}
