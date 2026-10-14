package application.domain.exceptions;

public class EmptyCartException extends DomainException {
    public EmptyCartException(String message) {
        super(message);
    }
}
