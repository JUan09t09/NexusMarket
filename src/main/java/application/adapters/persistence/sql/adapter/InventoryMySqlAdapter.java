package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.InventarioEntity;
import application.adapters.persistence.sql.repository.InventoryJpaRepository;
import application.domain.models.Inventario;
import application.domain.ports.out.InventoryRepositoryPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InventoryMySqlAdapter implements InventoryRepositoryPort {
    private final InventoryJpaRepository repo;

    @Override
    public Inventario save(Inventario inventory) {
        return toDomain(repo.save(toEntity(inventory)));
    }

    @Override
    public Optional<Inventario> findByProductAndStore(Inventario inventory) {
        if (inventory == null || inventory.getProductId() == null || inventory.getStoreId() == null) {
            return Optional.empty();
        }
        return repo.findByProductIdAndStoreId(inventory.getProductId(), inventory.getStoreId()).map(this::toDomain);
    }

    @Override
    public List<Inventario> findByProduct(Inventario inventory) {
        if (inventory == null || inventory.getProductId() == null) return List.of();
        return repo.findByProductId(inventory.getProductId()).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Inventario> findById(Inventario inventory) {
        if (inventory == null || inventory.getId() == null) return Optional.empty();
        return repo.findById(inventory.getId()).map(this::toDomain);
    }

    private InventarioEntity toEntity(Inventario i) {
        if (i == null) return null;
        InventarioEntity e = new InventarioEntity();
        e.setId(i.getId());
        e.setProductId(i.getProductId());
        e.setStoreId(i.getStoreId());
        e.setAmount(i.getAmount());
        e.setReserved(i.getReserved());
        e.setAvailable(i.isAvailable());
        return e;
    }

    private Inventario toDomain(InventarioEntity e) {
        if (e == null) return null;
        Inventario i = new Inventario();
        i.setId(e.getId());
        i.setProductId(e.getProductId());
        i.setStoreId(e.getStoreId());
        i.setAmount(e.getAmount());
        i.setReserved(e.getReserved());
        i.setAvailable(e.isAvailable());
        return i;
    }
}
