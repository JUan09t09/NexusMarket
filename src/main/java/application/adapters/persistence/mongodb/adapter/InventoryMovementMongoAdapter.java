package application.adapters.persistence.mongodb.adapter;

import application.adapters.persistence.mongodb.document.MotionDocument;
import application.adapters.persistence.mongodb.repository.InventoryMovementMongoRepository;
import application.domain.models.Motion;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InventoryMovementMongoAdapter implements InventoryMovementRepositoryPort {
    private final InventoryMovementMongoRepository repo;

    @Override
    public Motion save(Motion movement) {
        return toDomain(repo.save(toDoc(movement)));
    }

    private MotionDocument toDoc(Motion m) {
        if (m == null) return null;
        MotionDocument d = new MotionDocument();
        d.setId(m.getId());
        d.setInventoryId(m.getInventoryId());
        d.setMovementType(m.getMovementType());
        d.setQuantity(m.getQuantity());
        d.setDate(m.getDate());
        return d;
    }

    private Motion toDomain(MotionDocument d) {
        if (d == null) return null;
        Motion m = new Motion();
        m.setId(d.getId());
        m.setInventoryId(d.getInventoryId());
        m.setMovementType(d.getMovementType());
        m.setQuantity(d.getQuantity());
        m.setDate(d.getDate());
        return m;
    }
}
