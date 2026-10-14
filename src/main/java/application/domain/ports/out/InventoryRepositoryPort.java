package application.domain.ports.out;

import application.domain.models.Inventario;
import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {

    Inventario save(Inventario inventory);

    Optional<Inventario> findByProductAndStore(Inventario inventory);

    List<Inventario> findByProduct(Inventario inventory);

    Optional<Inventario> findById(Inventario inventory);
}
