package application.domain.exceptions;

public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
