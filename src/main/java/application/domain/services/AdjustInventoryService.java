package application.domain.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.exceptions.NegativeStockException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Inventario;
import application.domain.models.Motion;
import application.domain.models.User;
import application.domain.ports.in.AdjustInventoryUseCase;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.MovementType;

@Service
@RequiredArgsConstructor

public class AdjustInventoryService implements AdjustInventoryUseCase {
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Inventario execute(String operatorId, String productId, String storeId, int newAmount, String reason) {
        User operator = new User();
        operator.setIdentifier(operatorId);

        if (!userRepositoryPort.isActive(operator)) {
            throw new UnauthorizedUserException("El operador no existe o no esta autorizado.");
        }

        Inventario inventoryReference = new Inventario();
        inventoryReference.setProductId(productId);
        inventoryReference.setStoreId(storeId);

        Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference)
            .orElseThrow(() -> new InventoryNotFoundException(
                "No existe inventario para el producto en la bodega indicada."
            ));

        if (newAmount < 0) {
            throw new NegativeStockException("Las existencias no pueden ser negativas.");
        }

        int previous = inventory.getAmount() == null ? 0 : inventory.getAmount();
        int difference = newAmount - previous;

        inventory.setAmount(newAmount);
        Inventario savedInventory = inventoryRepositoryPort.save(inventory);

        Motion motion = new Motion();
        motion.setId(UUID.randomUUID().toString());
        motion.setInventoryId(savedInventory.getId());
        motion.setMovementType(MovementType.Adjustment);
        motion.setQuantity(difference);
        motion.setDate(LocalDate.now());
        inventoryMovementRepositoryPort.save(motion);

        return savedInventory;
    }
}
