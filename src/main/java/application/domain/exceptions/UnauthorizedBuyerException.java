package application.domain.exceptions;

public class UnauthorizedBuyerException extends DomainException {
    public UnauthorizedBuyerException(String message) {
        super(message);
    }
}
