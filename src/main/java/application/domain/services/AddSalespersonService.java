package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.StoreRepositoryPort;
import application.domain.models.User;
import application.domain.models.Store;

@Service 
@RequiredArgsConstructor 

public class AddSalespersonService {
    private final UserRepositoryPort userRepository;
    private final StoreRepositoryPort storeRepository;

    public User execute(
        String executorId,
        String tradeName,
        String mail,
        String wineryName,
        String typeWinery,
        String wineryAddress

    )
    if (!userRepository.userHastUserRole(executorId, "Administrator")) {
        throw new UnauthorizedException(
            "El usuario ejecutor con ID " + executorId + " no tiene permisos para agregar un vendedor."
        )
    }    
}
