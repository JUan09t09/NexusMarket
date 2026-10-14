package application.domain.ports.in;

import application.domain.models.User;

public interface AuthenticateUserUseCase {
    User execute(String mail, String password);
}
