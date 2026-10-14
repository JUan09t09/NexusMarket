package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.ProductEntity;
import application.adapters.persistence.sql.entity.VariantEntity;
import application.adapters.persistence.sql.repository.ProductJpaRepository;
import application.domain.models.Product;
import application.domain.models.Variant;
import application.domain.ports.out.ProductRepositoryPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductMySqlAdapter implements ProductRepositoryPort {
    private final ProductJpaRepository repo;

    @Override
    public Product save(Product product) {
        return toDomain(repo.save(toEntity(product)));
    }

    @Override
    public Optional<Product> findById(Product product) {
        if (product == null || product.getId() == null) return Optional.empty();
        return repo.findById(product.getId()).map(this::toDomain);
    }

    @Override
    public List<Product> findBySeller(Product product) {
        if (product == null || product.getSellerId() == null) return List.of();
        return repo.findBySellerId(product.getSellerId()).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsById(Product product) {
        if (product == null || product.getId() == null) return false;
        return repo.existsById(product.getId());
    }

    private ProductEntity toEntity(Product p) {
        if (p == null) return null;
        ProductEntity e = new ProductEntity();
        e.setId(p.getId());
        e.setName(p.getName());
        e.setDescription(p.getDescription());
        e.setBasePrice(p.getBasePrice());
        e.setSellerId(p.getSellerId());
        e.setStoreId(p.getStoreId());
        e.setProductType(p.getProductType());
        e.setStatus(p.getStatus());
        if (p.getVariants() != null) {
            for (Variant v : p.getVariants()) {
                VariantEntity ve = toVariantEntity(v, e);
                e.getVariants().add(ve);
            }
        }
        return e;
    }

    private Product toDomain(ProductEntity e) {
        if (e == null) return null;
        Product p = new Product();
        p.setId(e.getId());
        p.setName(e.getName());
        p.setDescription(e.getDescription());
        p.setBasePrice(e.getBasePrice());
        p.setSellerId(e.getSellerId());
        p.setStoreId(e.getStoreId());
        p.setProductType(e.getProductType());
        p.setStatus(e.getStatus());
        if (e.getVariants() != null) {
            for (VariantEntity ve : e.getVariants()) {
                p.getVariants().add(toVariantDomain(ve));
            }
        }
        return p;
    }

    private VariantEntity toVariantEntity(Variant v, ProductEntity parent) {
        if (v == null) return null;
        VariantEntity ve = new VariantEntity();
        ve.setId(v.getId());
        ve.setProductId(parent.getId());
        ve.setColor(v.getColor());
        ve.setSize(v.getSize());
        ve.setModel(v.getModel());
        ve.setQuantity(v.getQuantity());
        ve.setPrice(v.getPrice());
        ve.setProduct(parent);
        return ve;
    }

    private Variant toVariantDomain(VariantEntity ve) {
        if (ve == null) return null;
        Variant v = new Variant();
        v.setId(ve.getId());
        v.setProductId(ve.getProductId());
        v.setColor(ve.getColor());
        v.setSize(ve.getSize());
        v.setModel(ve.getModel());
        v.setQuantity(ve.getQuantity());
        v.setPrice(ve.getPrice());
        return v;
    }
}
