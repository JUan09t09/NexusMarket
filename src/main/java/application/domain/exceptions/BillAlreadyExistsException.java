package application.domain.exceptions;

public class BillAlreadyExistsException extends DomainException {
    public BillAlreadyExistsException(String message) {
        super(message);
    }
}
