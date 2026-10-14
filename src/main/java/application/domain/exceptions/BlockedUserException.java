package application.domain.exceptions;

public class BlockedUserException extends DomainException {
    public BlockedUserException(String message) {
        super(message);
    }
}
