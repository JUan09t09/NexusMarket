package application.domain.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidWarehouseException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Store;
import application.domain.models.User;
import application.domain.ports.in.CreateStoreUseCase;
import application.domain.ports.out.StoreRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.WarehouseType;

@Service
@RequiredArgsConstructor

public class CreateStoreService implements CreateStoreUseCase {
    private final StoreRepositoryPort storeRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Store execute(
        String executorAdminId,
        String sellerId,
        String warehouseName,
        WarehouseType warehouseType,
        String warehouseAddress
    ) {
        User executor = new User();
        executor.setIdentifier(executorAdminId);

        if (!userRepositoryPort.hasRole(executor, UserRole.Administrator)
                || !userRepositoryPort.isActive(executor)) {
            throw new UnauthorizedUserException(
                "El usuario ejecutor no tiene permisos para crear bodegas."
            );
        }

        if (warehouseType == null) {
            throw new InvalidWarehouseException("El tipo de bodega es obligatorio.");
        }

        if (warehouseType == WarehouseType.Seller) {
            User seller = new User();
            seller.setIdentifier(sellerId);
            if (sellerId == null || sellerId.isBlank()
                    || !userRepositoryPort.existsByIdentification(seller)) {
                throw new InvalidWarehouseException("El vendedor propietario indicado no es valido.");
            }
        }

        if (warehouseName == null || warehouseName.isBlank()
                || warehouseAddress == null || warehouseAddress.isBlank()) {
            throw new InvalidWarehouseException("El nombre y la direccion de la bodega son obligatorios.");
        }

        Store store = new Store();
        store.setId(UUID.randomUUID().toString());
        store.setName(warehouseName);
        store.setAddress(warehouseAddress);
        store.setWarehouseType(warehouseType);
        store.setSellerId(warehouseType == WarehouseType.Seller ? sellerId : null);
        store.setActive(true);

        return storeRepositoryPort.save(store);
    }
}
