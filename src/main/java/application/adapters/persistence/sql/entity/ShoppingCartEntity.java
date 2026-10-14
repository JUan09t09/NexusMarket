package application.adapters.persistence.sql.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
public class ShoppingCartEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Column(name = "store_id")
    private String storeId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItemEntity> items = new ArrayList<>();
}
