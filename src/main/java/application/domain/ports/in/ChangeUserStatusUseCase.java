package application.domain.ports.in;

import application.domain.models.User;
import application.domain.valueObjects.UserStatus;

public interface ChangeUserStatusUseCase {
    User execute(String executorAdminId, String userId, UserStatus newStatus);
}
