package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InsufficientStockException;
import application.domain.exceptions.InventoryNotAvailableException;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.models.Inventario;
import application.domain.models.ReserveResult;
import application.domain.models.Store;
import application.domain.ports.in.ReserveInventoryUseCase;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.StoreRepositoryPort;

@Service
@RequiredArgsConstructor

public class ReserveInventoryService implements ReserveInventoryUseCase {
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final StoreRepositoryPort storeRepositoryPort;

    @Override
    public ReserveResult execute(String productId, String storeId, int quantityReserve) {
        Inventario inventoryReference = new Inventario();
        inventoryReference.setProductId(productId);
        inventoryReference.setStoreId(storeId);

        Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference)
            .orElseThrow(() -> new InventoryNotFoundException(
                "No existe inventario para el producto en la bodega indicada."
            ));

        Store storeReference = new Store();
        storeReference.setId(storeId);

        boolean operativeStore = storeRepositoryPort.findById(storeReference)
            .map(Store::isActive)
            .orElse(false);

        if (!operativeStore || !inventory.isAvailable()) {
            throw new InventoryNotAvailableException("El inventario no esta disponible para reserva.");
        }

        int available = inventory.getAmount() == null ? 0 : inventory.getAmount();
        if (quantityReserve <= 0 || available < quantityReserve) {
            throw new InsufficientStockException("No hay existencias suficientes para cubrir la reserva.");
        }

        inventory.setAmount(available - quantityReserve);
        inventory.setReserved((inventory.getReserved() == null ? 0 : inventory.getReserved()) + quantityReserve);
        Inventario savedInventory = inventoryRepositoryPort.save(inventory);

        ReserveResult result = new ReserveResult();
        result.setProductId(productId);
        result.setStoreId(storeId);
        result.setQuantityReserved(quantityReserve);
        result.setAvailable(savedInventory.getAmount());
        result.setSuccess(true);
        return result;
    }
}
