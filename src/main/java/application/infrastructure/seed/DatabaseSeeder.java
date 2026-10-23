package application.infrastructure.seed;

import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.out.PasswordEncoderPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedBuyer();
        seedSeller();
    }

    private void seedAdmin() {
        if (exists("ADMIN001")) {
            return;
        }
        User admin = new User();
        admin.setIdentifier("ADMIN001");
        admin.setName("Administrador");
        admin.setEmail("admin@nexusmarket.com");
        admin.setRole(UserRole.Administrator);
        admin.setStatus(UserStatus.Active);
        admin.setPassword(passwordEncoderPort.encode(admin, "admin1234"));
        userRepositoryPort.save(admin);
        log.info("Seed: usuario Administrador creado (ADMIN001 / admin1234)");
    }

    private void seedBuyer() {
        if (exists("BUYER001")) {
            return;
        }
        Buyer buyer = new Buyer();
        buyer.setIdentifier("BUYER001");
        buyer.setName("Comprador de Prueba");
        buyer.setEmail("buyer@nexusmarket.com");
        buyer.setRole(UserRole.Buyer);
        buyer.setStatus(UserStatus.Active);
        buyer.setMainAdress("Calle 1 # 2-3");
        buyer.setPassword(passwordEncoderPort.encode(buyer, "buyer1234"));
        userRepositoryPort.save(buyer);
        log.info("Seed: usuario Buyer creado (BUYER001 / buyer1234)");
    }

    private void seedSeller() {
        if (exists("SELLER001")) {
            return;
        }
        User seller = new User();
        seller.setIdentifier("SELLER001");
        seller.setName("Vendedor de Prueba");
        seller.setEmail("seller@nexusmarket.com");
        seller.setRole(UserRole.Seller);
        seller.setStatus(UserStatus.Active);
        seller.setPassword(passwordEncoderPort.encode(seller, "SELLER001"));
        userRepositoryPort.save(seller);
        log.info("Seed: usuario Seller creado (SELLER001 / SELLER001)");
    }

    private boolean exists(String identifier) {
        User reference = new User();
        reference.setIdentifier(identifier);
        return userRepositoryPort.existsByIdentification(reference);
    }
}
