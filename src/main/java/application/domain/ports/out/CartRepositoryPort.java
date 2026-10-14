package application.domain.ports.out;

import application.domain.models.ShoppingCart;
import java.util.Optional;

public interface CartRepositoryPort {

    ShoppingCart save(ShoppingCart cart);

    Optional<ShoppingCart> findById(ShoppingCart cart);

    Optional<ShoppingCart> findByBuyer(ShoppingCart cart);
}
