package application.domain.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.exceptions.NegativeStockException;
import application.domain.exceptions.ReservationNotFoundException;
import application.domain.models.Inventario;
import application.domain.models.Motion;
import application.domain.ports.in.ConfirmSaleOutputUseCase;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueObjects.MovementType;

@Service
@RequiredArgsConstructor

public class ConfirmSaleOutputService implements ConfirmSaleOutputUseCase {
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;

    @Override
    public Inventario execute(String productId, String storeId, int quantity, String orderId) {
        Inventario inventoryReference = new Inventario();
        inventoryReference.setProductId(productId);
        inventoryReference.setStoreId(storeId);

        Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference)
            .orElseThrow(() -> new InventoryNotFoundException(
                "No existe inventario para el producto en la bodega indicada."
            ));

        int reserved = inventory.getReserved() == null ? 0 : inventory.getReserved();
        if (reserved < quantity) {
            throw new ReservationNotFoundException("El pedido no cuenta con una reserva previa suficiente.");
        }

        int amount = inventory.getAmount() == null ? 0 : inventory.getAmount();
        if (amount - quantity < 0) {
            throw new NegativeStockException("La salida dejaria las existencias por debajo de cero.");
        }

        inventory.setAmount(amount - quantity);
        inventory.setReserved(reserved - quantity);
        Inventario savedInventory = inventoryRepositoryPort.save(inventory);

        Motion motion = new Motion();
        motion.setId(UUID.randomUUID().toString());
        motion.setInventoryId(savedInventory.getId());
        motion.setMovementType(MovementType.SaleOutput);
        motion.setQuantity(quantity);
        motion.setDate(LocalDate.now());
        inventoryMovementRepositoryPort.save(motion);

        return savedInventory;
    }
}
