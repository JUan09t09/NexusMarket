package application.domain.ports.out;

import application.domain.models.User;

public interface SellerRepositoryPort {

    boolean exists(User seller);

    boolean isActive(User seller);
}
