package application.adapters.persistence.sql.entity;

import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    private String name;
    private String description;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "store_id")
    private String storeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VariantEntity> variants = new ArrayList<>();
}
