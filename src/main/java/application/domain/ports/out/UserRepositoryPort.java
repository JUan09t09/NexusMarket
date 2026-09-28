package application.domain.ports.out;

import application.domain.valueObjects.UserRole;

public interface UserRepositoryPort {

    boolean userHastUserRole(String userId, UserRole role);
}
