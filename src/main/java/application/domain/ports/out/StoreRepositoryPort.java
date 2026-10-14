package application.domain.ports.out;

import application.domain.models.Store;
import java.util.List;
import java.util.Optional;

public interface StoreRepositoryPort {

    Store save(Store store);

    Optional<Store> findById(Store store);

    List<Store> findByOwner(Store store);

    boolean existsById(Store store);

    boolean isActive(Store store);
}
