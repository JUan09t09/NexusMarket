package application.domain.exceptions;

public class CartItemNotFoundException extends DomainException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
