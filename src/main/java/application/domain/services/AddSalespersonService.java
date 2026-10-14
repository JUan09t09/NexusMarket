package application.domain.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidWarehouseException;
import application.domain.exceptions.SellerExistsException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.models.Store;
import application.domain.models.User;
import application.domain.ports.in.AddSalespersonUseCase;
import application.domain.ports.out.StoreRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import application.domain.valueObjects.WarehouseType;

@Service
@RequiredArgsConstructor

public class AddSalespersonService implements AddSalespersonUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final StoreRepositoryPort storeRepositoryPort;

    @Override
    public User execute(
        String executorAdminId,
        String tradeName,
        String documentId,
        String mail,
        String wineryName,
        String typeWinery,
        String wineryAddress

    ) {
        User executor = userReference(executorAdminId);
        if (!userRepositoryPort.hasRole(executor, UserRole.Administrator)
                || !userRepositoryPort.isActive(executor)) {
            throw new UnauthorizedUserException(
                "El usuario ejecutor con ID " + executorAdminId + " no tiene permisos para agregar un vendedor."
            );
        }

        User byIdentification = userReference(documentId);
        User byEmail = userEmailReference(mail);
        if (userRepositoryPort.existsByEmail(byEmail)
                || userRepositoryPort.existsByIdentification(byIdentification)) {
            throw new SellerExistsException(
                "Ya existe un usuario registrado con el correo o el identificador indicado."
            );
        }

        if (wineryName == null || wineryName.isBlank() || typeWinery == null || typeWinery.isBlank()) {
            throw new InvalidWarehouseException(
                "Los datos de la bodega inicial del vendedor son incompletos."
            );
        }

        WarehouseType warehouseType;
        if (typeWinery.equalsIgnoreCase("Marketplace")) {
            warehouseType = WarehouseType.Marketplace;
        } else if (typeWinery.equalsIgnoreCase("Vendedor") || typeWinery.equalsIgnoreCase("Seller")) {
            warehouseType = WarehouseType.Seller;
        } else {
            throw new InvalidWarehouseException("Tipo de bodega no valido: " + typeWinery);
        }

        User seller = new User();
        seller.setIdentifier(documentId);
        seller.setName(tradeName);
        seller.setEmail(mail);
        seller.setRole(UserRole.Seller);
        seller.setStatus(UserStatus.Active);
        User savedSeller = userRepositoryPort.save(seller);

        Store store = new Store();
        store.setId(UUID.randomUUID().toString());
        store.setName(wineryName);
        store.setAddress(wineryAddress);
        store.setSellerId(savedSeller.getIdentifier());
        store.setWarehouseType(warehouseType);
        store.setActive(true);
        storeRepositoryPort.save(store);

        return savedSeller;
    }

    private User userReference(String identification) {
        User user = new User();
        user.setIdentifier(identification);
        return user;
    }

    private User userEmailReference(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }
}
