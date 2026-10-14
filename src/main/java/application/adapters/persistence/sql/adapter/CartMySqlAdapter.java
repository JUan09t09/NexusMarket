package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.CartItemEntity;
import application.adapters.persistence.sql.entity.ShoppingCartEntity;
import application.adapters.persistence.sql.repository.CartJpaRepository;
import application.domain.models.ShoppingCart;
import application.domain.models.Variant;
import application.domain.ports.out.CartRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartMySqlAdapter implements CartRepositoryPort {
    private final CartJpaRepository repo;

    @Override
    public ShoppingCart save(ShoppingCart cart) {
        return toDomain(repo.save(toEntity(cart)));
    }

    @Override
    public Optional<ShoppingCart> findById(ShoppingCart cart) {
        if (cart == null || cart.getId() == null) return Optional.empty();
        return repo.findById(cart.getId()).map(this::toDomain);
    }

    @Override
    public Optional<ShoppingCart> findByBuyer(ShoppingCart cart) {
        if (cart == null || cart.getBuyerId() == null) return Optional.empty();
        return repo.findByBuyerId(cart.getBuyerId()).map(this::toDomain);
    }

    private ShoppingCartEntity toEntity(ShoppingCart c) {
        if (c == null) return null;
        ShoppingCartEntity e = new ShoppingCartEntity();
        e.setId(c.getId());
        e.setBuyerId(c.getBuyerId());
        if (c.getItems() != null) {
            for (Variant v : c.getItems()) {
                CartItemEntity ie = new CartItemEntity();
                ie.setId(v.getId());
                ie.setCartId(c.getId());
                ie.setProductId(v.getProductId());
                ie.setVariantId(v.getId());
                ie.setQuantity(v.getQuantity());
                ie.setPrice(v.getPrice());
                ie.setCart(e);
                e.getItems().add(ie);
            }
        }
        return e;
    }

    private ShoppingCart toDomain(ShoppingCartEntity e) {
        if (e == null) return null;
        ShoppingCart c = new ShoppingCart();
        c.setId(e.getId());
        c.setBuyerId(e.getBuyerId());
        if (e.getItems() != null) {
            for (CartItemEntity ie : e.getItems()) {
                Variant v = new Variant();
                v.setId(ie.getVariantId());
                v.setProductId(ie.getProductId());
                v.setQuantity(ie.getQuantity());
                v.setPrice(ie.getPrice());
                c.getItems().add(v);
            }
        }
        return c;
    }
}
