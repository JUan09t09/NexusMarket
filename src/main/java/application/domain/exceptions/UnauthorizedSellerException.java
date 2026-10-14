package application.domain.exceptions;

public class UnauthorizedSellerException extends DomainException {
    public UnauthorizedSellerException(String message) {
        super(message);
    }
}
