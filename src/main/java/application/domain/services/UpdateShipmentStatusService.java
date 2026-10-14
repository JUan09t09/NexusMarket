package application.domain.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidShippingStatusTransitionException;
import application.domain.exceptions.ShipmentNotFoundException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Shipment;
import application.domain.models.User;
import application.domain.ports.in.UpdateShipmentStatusUseCase;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.ShippingStatus;
import application.domain.valueObjects.UserRole;

@Service
@RequiredArgsConstructor

public class UpdateShipmentStatusService implements UpdateShipmentStatusUseCase {
    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Shipment execute(long shipmentId, String operatorId, ShippingStatus newStatus, LocalDate deliveryDate) {
        User operator = new User();
        operator.setIdentifier(operatorId);

        if (!userRepositoryPort.hasRole(operator, UserRole.LogisticOperator)) {
            throw new UnauthorizedUserException("El operador no tiene rol de operador logistico.");
        }

        Shipment shipmentReference = new Shipment();
        shipmentReference.setIdentifier(shipmentId);

        Shipment shipment = shipmentRepositoryPort.findById(shipmentReference)
            .orElseThrow(() -> new ShipmentNotFoundException("No existe el envio con ID " + shipmentId + "."));

        if (!isValidTransition(shipment.getShippingStatus(), newStatus)) {
            throw new InvalidShippingStatusTransitionException(
                "La transicion de estado del envio no es valida."
            );
        }

        shipment.setShippingStatus(newStatus);
        if (newStatus == ShippingStatus.DELIVERED) {
            shipment.setDeliveryDate(deliveryDate != null ? deliveryDate : LocalDate.now());
        }

        return shipmentRepositoryPort.save(shipment);
    }

    private boolean isValidTransition(ShippingStatus current, ShippingStatus next) {
        if (current == null || next == null) {
            return false;
        }
        switch (current) {
            case PENDING:
                return next == ShippingStatus.SHIPPED;
            case SHIPPED:
                return next == ShippingStatus.DELIVERED || next == ShippingStatus.RETURNED;
            default:
                return false;
        }
    }
}
