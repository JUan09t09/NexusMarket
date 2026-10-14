package application.domain.exceptions;

public class VariantNotFoundException extends DomainException {
    public VariantNotFoundException(String message) {
        super(message);
    }
}
