package application.domain.exceptions;

public class InvalidRefundAmountException extends DomainException {
    public InvalidRefundAmountException(String message) {
        super(message);
    }
}
