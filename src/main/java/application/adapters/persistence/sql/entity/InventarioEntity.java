package application.adapters.persistence.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventories")
@Getter
@Setter
public class InventarioEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "store_id")
    private String storeId;

    private Integer amount;
    private Integer reserved;
    private boolean available;
}
