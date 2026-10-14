package application.domain.ports.out;

import application.domain.models.Shipment;
import java.util.Optional;

public interface ShipmentRepositoryPort {

    Shipment save(Shipment shipment);

    Optional<Shipment> findById(Shipment shipment);

    Optional<Shipment> findByOrder(Shipment shipment);

    boolean existsByOrder(Shipment shipment);
}
