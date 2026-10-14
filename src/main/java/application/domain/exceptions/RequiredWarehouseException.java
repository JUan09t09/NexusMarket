package application.domain.exceptions;

public class RequiredWarehouseException extends DomainException {
    public RequiredWarehouseException(String message) {
        super(message);
    }
}
