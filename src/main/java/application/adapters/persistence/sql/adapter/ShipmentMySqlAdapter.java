package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.ShipmentEntity;
import application.adapters.persistence.sql.repository.ShipmentJpaRepository;
import application.domain.models.Shipment;
import application.domain.ports.out.ShipmentRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShipmentMySqlAdapter implements ShipmentRepositoryPort {
    private final ShipmentJpaRepository repo;

    @Override
    public Shipment save(Shipment shipment) {
        return toDomain(repo.save(toEntity(shipment)));
    }

    @Override
    public Optional<Shipment> findById(Shipment shipment) {
        if (shipment == null || shipment.getIdentifier() == 0) return Optional.empty();
        return repo.findByIdentifier(shipment.getIdentifier()).map(this::toDomain);
    }

    @Override
    public Optional<Shipment> findByOrder(Shipment shipment) {
        if (shipment == null || shipment.getOrderId() == null) return Optional.empty();
        return repo.findByOrderId(shipment.getOrderId()).map(this::toDomain);
    }

    @Override
    public boolean existsByOrder(Shipment shipment) {
        if (shipment == null || shipment.getOrderId() == null) return false;
        return repo.existsByOrderId(shipment.getOrderId());
    }

    private ShipmentEntity toEntity(Shipment s) {
        if (s == null) return null;
        ShipmentEntity e = new ShipmentEntity();
        e.setIdentifier(s.getIdentifier());
        e.setOrderId(s.getOrderId());
        e.setBuyerId(s.getBuyerId());
        e.setShippingStatus(s.getShippingStatus());
        e.setShippingDate(s.getShippingDate());
        e.setDeliveryDate(s.getDeliveryDate());
        return e;
    }

    private Shipment toDomain(ShipmentEntity e) {
        if (e == null) return null;
        Shipment s = new Shipment();
        s.setIdentifier(e.getIdentifier());
        s.setOrderId(e.getOrderId());
        s.setBuyerId(e.getBuyerId());
        s.setShippingStatus(e.getShippingStatus());
        s.setShippingDate(e.getShippingDate());
        s.setDeliveryDate(e.getDeliveryDate());
        return s;
    }
}
