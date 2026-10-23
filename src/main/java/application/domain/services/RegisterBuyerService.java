package application.domain.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.BuyerExistsException;
import application.domain.exceptions.InvalidBuyerDataException;
import application.domain.exceptions.InvalidPasswordException;
import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.in.RegisterBuyerUseCase;
import application.domain.ports.out.PasswordEncoderPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

@Service
@RequiredArgsConstructor

public class RegisterBuyerService implements RegisterBuyerUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public Buyer execute(String name, String documentId, String mail, String password, String mainAddress) {
        if (name == null || name.isBlank() || mail == null || !mail.contains("@") || mainAddress == null || mainAddress.isBlank()) {
            throw new InvalidBuyerDataException("Los datos basicos del comprador son invalidos.");
        }

        if (password == null || password.length() < 8) {
            throw new InvalidPasswordException("La contrasena no cumple las politicas minimas de seguridad.");
        }

        User byIdentification = new User();
        byIdentification.setIdentifier(documentId);
        User byEmail = new User();
        byEmail.setEmail(mail);

        if (userRepositoryPort.existsByEmail(byEmail)
                || userRepositoryPort.existsByIdentification(byIdentification)) {
            throw new BuyerExistsException("Ya existe un comprador con el correo o el identificador indicado.");
        }

        Buyer buyer = new Buyer();
        buyer.setIdentifier(documentId);
        buyer.setName(name);
        buyer.setEmail(mail);
        buyer.setPassword(passwordEncoderPort.encode(buyer, password));
        buyer.setRole(UserRole.Buyer);
        buyer.setStatus(UserStatus.Active);
        buyer.setMainAdress(mainAddress);

        return (Buyer) userRepositoryPort.save(buyer);
    }
}
