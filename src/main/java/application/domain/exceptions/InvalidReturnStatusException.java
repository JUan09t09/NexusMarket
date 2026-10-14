package application.domain.exceptions;

public class InvalidReturnStatusException extends DomainException {
    public InvalidReturnStatusException(String message) {
        super(message);
    }
}
