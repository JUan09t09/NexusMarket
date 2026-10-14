package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InventoryNotAvailableException;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.exceptions.InvalidReservationQuantityException;
import application.domain.models.Inventario;
import application.domain.ports.in.ReleaseInventoryReservationUseCase;
import application.domain.ports.out.InventoryRepositoryPort;

@Service
@RequiredArgsConstructor

public class ReleaseInventoryReservationService implements ReleaseInventoryReservationUseCase {
    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public Inventario execute(String productId, String storeId, int quantity) {
        Inventario inventoryReference = new Inventario();
        inventoryReference.setProductId(productId);
        inventoryReference.setStoreId(storeId);

        Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference)
            .orElseThrow(() -> new InventoryNotFoundException(
                "No existe inventario para el producto en la bodega indicada."
            ));

        int reserved = inventory.getReserved() == null ? 0 : inventory.getReserved();
        if (quantity > reserved) {
            throw new InvalidReservationQuantityException(
                "La cantidad a liberar supera las unidades actualmente reservadas."
            );
        }

        if (!inventory.isAvailable()) {
            throw new InventoryNotAvailableException("El inventario no esta disponible.");
        }

        inventory.setReserved(reserved - quantity);
        return inventoryRepositoryPort.save(inventory);
    }
}
