package application.domain.ports.out;
import application.domain.models.Store;
import java.util.Optional;

public interface StoreRepositoryPort {

    Store save(Store store);
    Optional<Store> findById(String storeId);
    boolean existsById(String storeId);
    boolean isStoreActive(String storeId);
}
