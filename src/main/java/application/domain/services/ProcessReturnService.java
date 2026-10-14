package application.domain.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidReturnStatusException;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Inventario;
import application.domain.models.Motion;
import application.domain.models.Return;
import application.domain.models.User;
import application.domain.ports.in.ProcessReturnUseCase;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.MovementType;
import application.domain.valueObjects.ReturnStatus;

@Service
@RequiredArgsConstructor

public class ProcessReturnService implements ProcessReturnUseCase {
    private final ReturnRepositoryPort returnRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Return execute(String returnId, String operatorId, String storeId) {
        User operator = new User();
        operator.setIdentifier(operatorId);

        if (!userRepositoryPort.isActive(operator)) {
            throw new UnauthorizedUserException("El operador no esta autorizado para procesar devoluciones.");
        }

        Return returnReference = new Return();
        returnReference.setId(returnId);

        Return returnRequest = returnRepositoryPort.findById(returnReference)
            .orElseThrow(() -> new InvalidReturnStatusException("No existe la devolucion con ID " + returnId + "."));

        if (returnRequest.getReturnStatus() != ReturnStatus.Pending) {
            throw new InvalidReturnStatusException("La devolucion no se encuentra en estado pendiente.");
        }

        Inventario inventoryReference = new Inventario();
        inventoryReference.setProductId(returnRequest.getProductId());
        inventoryReference.setStoreId(storeId);

        Inventario inventory = inventoryRepositoryPort.findByProductAndStore(inventoryReference)
            .orElseThrow(() -> new InventoryNotFoundException(
                "No existe inventario para la combinacion de producto y bodega."
            ));

        int amount = inventory.getAmount() == null ? 0 : inventory.getAmount();
        inventory.setAmount(amount + returnRequest.getQuantity());
        Inventario savedInventory = inventoryRepositoryPort.save(inventory);

        Motion motion = new Motion();
        motion.setId(UUID.randomUUID().toString());
        motion.setInventoryId(savedInventory.getId());
        motion.setMovementType(MovementType.Return);
        motion.setQuantity(returnRequest.getQuantity());
        motion.setDate(LocalDate.now());
        inventoryMovementRepositoryPort.save(motion);

        returnRequest.setReturnStatus(ReturnStatus.Approved);
        return returnRepositoryPort.save(returnRequest);
    }
}
