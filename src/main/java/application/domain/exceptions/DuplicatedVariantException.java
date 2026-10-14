package application.domain.exceptions;

public class DuplicatedVariantException extends DomainException {
    public DuplicatedVariantException(String message) {
        super(message);
    }
}
