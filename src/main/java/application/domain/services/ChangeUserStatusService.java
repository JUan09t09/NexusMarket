package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidUserStatusException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.exceptions.UserNotFoundException;
import application.domain.models.User;
import application.domain.ports.in.ChangeUserStatusUseCase;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

@Service
@RequiredArgsConstructor

public class ChangeUserStatusService implements ChangeUserStatusUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(String executorAdminId, String userId, UserStatus newStatus) {
        User executor = new User();
        executor.setIdentifier(executorAdminId);

        if (!userRepositoryPort.hasRole(executor, UserRole.Administrator)
                || !userRepositoryPort.isActive(executor)) {
            throw new UnauthorizedUserException(
                "El usuario ejecutor no tiene permisos para cambiar el estado de otro usuario."
            );
        }

        User target = new User();
        target.setIdentifier(userId);

        User user = userRepositoryPort.findByIdentification(target)
            .orElseThrow(() -> new UserNotFoundException("No existe el usuario con ID " + userId + "."));

        if (newStatus == null) {
            throw new InvalidUserStatusException("El estado indicado no es un valor valido del catalogo.");
        }

        user.setStatus(newStatus);
        return userRepositoryPort.save(user);
    }
}
