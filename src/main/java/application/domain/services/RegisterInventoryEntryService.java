package application.domain.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.exceptions.InvalidMovementQuantityException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Inventario;
import application.domain.models.Motion;
import application.domain.models.User;
import application.domain.ports.in.RegisterInventoryEntryUseCase;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.MovementType;
import application.domain.valueObjects.UserRole;

@Service
@RequiredArgsConstructor

public class RegisterInventoryEntryService implements RegisterInventoryEntryUseCase {
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Inventario execute(String operatorId, String productId, String storeId, int quantity) {
        User operator = new User();
        operator.setIdentifier(operatorId);

        boolean authorizedRole = userRepositoryPort.hasRole(operator, UserRole.Seller)
            || userRepositoryPort.hasRole(operator, UserRole.LogisticOperator);

        if (!authorizedRole || !userRepositoryPort.isActive(operator)) {
            throw new UnauthorizedUserException("El operador no esta autorizado para registrar ingresos de inventario.");
        }

        Inventario inventoryReference = new Inventario();
        inventoryReference.setProductId(productId);
        inventoryReference.setStoreId(storeId);

        Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference)
            .orElseThrow(() -> new InventoryNotFoundException(
                "No existe inventario para el producto en la bodega indicada."
            ));

        if (quantity <= 0) {
            throw new InvalidMovementQuantityException("La cantidad a ingresar debe ser mayor que cero.");
        }

        int amount = inventory.getAmount() == null ? 0 : inventory.getAmount();
        inventory.setAmount(amount + quantity);
        Inventario savedInventory = inventoryRepositoryPort.save(inventory);

        Motion motion = new Motion();
        motion.setId(UUID.randomUUID().toString());
        motion.setInventoryId(savedInventory.getId());
        motion.setMovementType(MovementType.Inbound);
        motion.setQuantity(quantity);
        motion.setDate(LocalDate.now());
        inventoryMovementRepositoryPort.save(motion);

        return savedInventory;
    }
}
