package application.domain.ports.in;

import application.domain.models.User;

public interface AddSalespersonUseCase {
    User execute(
        String executorAdminId,
        String tradeName,
        String documentId,
        String mail,
        String wineryName,
        String typeWinery,
        String wineryAddress
    );
}
