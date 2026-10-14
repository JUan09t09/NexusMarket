package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.StoreEntity;
import application.adapters.persistence.sql.repository.StoreJpaRepository;
import application.domain.models.Store;
import application.domain.ports.out.StoreRepositoryPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreMySqlAdapter implements StoreRepositoryPort {
    private final StoreJpaRepository repo;

    @Override
    public Store save(Store store) {
        return toDomain(repo.save(toEntity(store)));
    }

    @Override
    public Optional<Store> findById(Store store) {
        if (store == null || store.getId() == null) return Optional.empty();
        return repo.findById(store.getId()).map(this::toDomain);
    }

    @Override
    public List<Store> findByOwner(Store store) {
        if (store == null || store.getSellerId() == null) return List.of();
        return repo.findBySellerId(store.getSellerId()).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsById(Store store) {
        if (store == null || store.getId() == null) return false;
        return repo.existsById(store.getId());
    }

    @Override
    public boolean isActive(Store store) {
        return findById(store).map(Store::isActive).orElse(false);
    }

    private StoreEntity toEntity(Store s) {
        if (s == null) return null;
        StoreEntity e = new StoreEntity();
        e.setId(s.getId());
        e.setName(s.getName());
        e.setAddress(s.getAddress());
        e.setSellerId(s.getSellerId());
        e.setActive(s.isActive());
        e.setWarehouseType(s.getWarehouseType());
        return e;
    }

    private Store toDomain(StoreEntity e) {
        if (e == null) return null;
        Store s = new Store();
        s.setId(e.getId());
        s.setName(e.getName());
        s.setAddress(e.getAddress());
        s.setSellerId(e.getSellerId());
        s.setActive(e.isActive());
        s.setWarehouseType(e.getWarehouseType());
        return s;
    }
}
