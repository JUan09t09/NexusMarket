package application.domain.exceptions;

public class InvalidBuyerDataException extends DomainException {
    public InvalidBuyerDataException(String message) {
        super(message);
    }
}
