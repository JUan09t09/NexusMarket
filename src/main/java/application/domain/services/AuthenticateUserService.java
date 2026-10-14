package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.BlockedUserException;
import application.domain.exceptions.InvalidCredentialsException;
import application.domain.models.User;
import application.domain.ports.in.AuthenticateUserUseCase;
import application.domain.ports.out.PasswordEncoderPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserStatus;

@Service
@RequiredArgsConstructor

public class AuthenticateUserService implements AuthenticateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public User execute(String mail, String password) {
        User mailReference = new User();
        mailReference.setEmail(mail);

        User user = userRepositoryPort.findByEmail(mailReference)
            .orElseThrow(() -> new InvalidCredentialsException("Credenciales invalidas."));

        if (password == null || !passwordEncoderPort.matches(user, password)) {
            throw new InvalidCredentialsException("Credenciales invalidas.");
        }

        if (user.getStatus() == UserStatus.Blocked || user.getStatus() == UserStatus.Suspended) {
            throw new BlockedUserException("El usuario se encuentra bloqueado o suspendido.");
        }

        return user;
    }
}
