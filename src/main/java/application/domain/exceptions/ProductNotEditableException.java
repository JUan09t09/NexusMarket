package application.domain.exceptions;

public class ProductNotEditableException extends DomainException {
    public ProductNotEditableException(String message) {
        super(message);
    }
}
