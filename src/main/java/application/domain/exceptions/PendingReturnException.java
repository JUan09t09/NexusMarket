package application.domain.exceptions;

public class PendingReturnException extends DomainException {
    public PendingReturnException(String message) {
        super(message);
    }
}
