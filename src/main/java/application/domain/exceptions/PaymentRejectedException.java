package application.domain.exceptions;

public class PaymentRejectedException extends DomainException {
    public PaymentRejectedException(String message) {
        super(message);
    }
}
