package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.BuyerEntity;
import application.adapters.persistence.sql.entity.UserEntity;
import application.adapters.persistence.sql.repository.UserJpaRepository;
import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserRole;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMySqlAdapter implements UserRepositoryPort {
    private final UserJpaRepository repo;

    @Override
    public User save(User user) {
        if (user instanceof Buyer b) {
            BuyerEntity e = toBuyerEntity(b);
            return toBuyerDomain(repo.save(e));
        }
        UserEntity e = toUserEntity(user);
        return toUserDomain(repo.save(e));
    }

    @Override
    public Optional<User> findByIdentification(User user) {
        if (user == null || user.getIdentifier() == null) return Optional.empty();
        return repo.findByIdentifier(user.getIdentifier()).map(this::toUserDomain);
    }

    @Override
    public Optional<User> findByEmail(User user) {
        if (user == null || user.getEmail() == null) return Optional.empty();
        return repo.findByEmail(user.getEmail()).map(this::toUserDomain);
    }

    @Override
    public boolean existsByIdentification(User user) {
        if (user == null || user.getIdentifier() == null) return false;
        return repo.existsByIdentifier(user.getIdentifier());
    }

    @Override
    public boolean existsByEmail(User user) {
        if (user == null || user.getEmail() == null) return false;
        return repo.existsByEmail(user.getEmail());
    }

    @Override
    public boolean hasRole(User user, UserRole role) {
        return findByIdentification(user).map(u -> u.getRole() == role).orElse(false);
    }

    @Override
    public boolean isActive(User user) {
        return findByIdentification(user).map(u -> u.getStatus() == application.domain.valueObjects.UserStatus.Active).orElse(false);
    }

    private UserEntity toUserEntity(User u) {
        if (u == null) return null;
        UserEntity e = new UserEntity();
        e.setIdentifier(u.getIdentifier());
        e.setName(u.getName());
        e.setEmail(u.getEmail());
        e.setRole(u.getRole());
        e.setStatus(u.getStatus());
        e.setPassword(u.getPassword());
        return e;
    }

    private BuyerEntity toBuyerEntity(Buyer b) {
        if (b == null) return null;
        BuyerEntity e = new BuyerEntity();
        e.setIdentifier(b.getIdentifier());
        e.setName(b.getName());
        e.setEmail(b.getEmail());
        e.setRole(b.getRole());
        e.setStatus(b.getStatus());
        e.setPassword(b.getPassword());
        e.setMainAdress(b.getMainAdress());
        e.setAdditionalAdress(b.getAdditionalAdress());
        e.setComersialStatus(b.getComersialStatus());
        return e;
    }

    private User toUserDomain(UserEntity e) {
        if (e == null) return null;
        if (e instanceof BuyerEntity be) return toBuyerDomain(be);
        User u = new User();
        u.setIdentifier(e.getIdentifier());
        u.setName(e.getName());
        u.setEmail(e.getEmail());
        u.setRole(e.getRole());
        u.setStatus(e.getStatus());
        u.setPassword(e.getPassword());
        return u;
    }

    private Buyer toBuyerDomain(BuyerEntity e) {
        if (e == null) return null;
        Buyer b = new Buyer();
        b.setIdentifier(e.getIdentifier());
        b.setName(e.getName());
        b.setEmail(e.getEmail());
        b.setRole(e.getRole());
        b.setStatus(e.getStatus());
        b.setPassword(e.getPassword());
        b.setMainAdress(e.getMainAdress());
        b.setAdditionalAdress(e.getAdditionalAdress());
        b.setComersialStatus(e.getComersialStatus());
        return b;
    }
}
