package application.domain.exceptions;

public class SellerExistsException extends DomainException {
    public SellerExistsException(String message) {
        super(message);
    }
}
