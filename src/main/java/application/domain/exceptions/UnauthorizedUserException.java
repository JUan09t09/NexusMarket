package application.domain.exceptions;

public class UnauthorizedUserException extends DomainException {
    public UnauthorizedUserException(String message) {
        super(message);
    }
}
