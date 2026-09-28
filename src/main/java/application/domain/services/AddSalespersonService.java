package application.domain.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.StoreRepositoryPort;
import application.domain.valueObjects.UserRole;

@Service
@RequiredArgsConstructor

public class AddSalespersonService {
    private final UserRepositoryPort userRepositoryPort;
    private final StoreRepositoryPort storeRepositoryPort;

    public User execute(
        String executorAdminId,
        String tradeName,
        String documentId,
        String mail,
        String wineryName,
        String typeWinery,
        String wineryAddress

    ) {
        if (!userRepositoryPort.userHastUserRole(executorAdminId, UserRole.Administrator)) {
            throw new AccessDeniedException(
                "El usuario ejecutor con ID " + executorAdminId + " no tiene permisos para agregar un vendedor."
            );
        }
        throw new UnsupportedOperationException(
            "Pendiente: implementar el camino feliz del SDD AddSalespersonService (pasos 2 a 5)."
        );
    }
}
