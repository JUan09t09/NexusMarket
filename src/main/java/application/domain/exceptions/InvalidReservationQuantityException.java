package application.domain.exceptions;

public class InvalidReservationQuantityException extends DomainException {
    public InvalidReservationQuantityException(String message) {
        super(message);
    }
}
