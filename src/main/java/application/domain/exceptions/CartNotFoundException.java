package application.domain.exceptions;

public class CartNotFoundException extends DomainException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
