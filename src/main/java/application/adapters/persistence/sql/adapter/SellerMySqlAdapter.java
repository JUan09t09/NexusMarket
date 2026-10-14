package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.UserEntity;
import application.adapters.persistence.sql.repository.UserJpaRepository;
import application.domain.models.User;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.valueObjects.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerMySqlAdapter implements SellerRepositoryPort {
    private final UserJpaRepository repo;

    @Override
    public boolean exists(User seller) {
        if (seller == null || seller.getIdentifier() == null) return false;
        return repo.existsByIdentifier(seller.getIdentifier());
    }

    @Override
    public boolean isActive(User seller) {
        if (seller == null || seller.getIdentifier() == null) return false;
        return repo.findByIdentifier(seller.getIdentifier())
            .map(UserEntity::getStatus)
            .filter(s -> s == UserStatus.Active)
            .isPresent();
    }
}
