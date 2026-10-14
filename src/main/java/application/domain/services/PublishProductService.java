package application.domain.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import application.domain.exceptions.InvalidProductDataException;
import application.domain.exceptions.RequiredWarehouseException;
import application.domain.exceptions.UnauthorizedSellerException;
import application.domain.models.Product;
import application.domain.models.Store;
import application.domain.models.User;
import application.domain.models.Variant;
import application.domain.ports.in.PublishProductUseCase;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.ports.out.StoreRepositoryPort;
import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;

@Service
@RequiredArgsConstructor

public class PublishProductService implements PublishProductUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final StoreRepositoryPort storeRepositoryPort;

    @Override
    public Product execute(
        String productId,
        String sellerId,
        ProductType productType,
        String name,
        String description,
        BigDecimal basePrice,
        String storeId,
        List<Variant> variants
    ) {
        User sellerReference = new User();
        sellerReference.setIdentifier(sellerId);

        if (!sellerRepositoryPort.isActive(sellerReference)) {
            throw new UnauthorizedSellerException("El vendedor no existe o no esta habilitado.");
        }

        if (productType == ProductType.Physical) {
            Store storeReference = new Store();
            storeReference.setId(storeId);
            if (storeId == null || storeId.isBlank()
                    || !storeRepositoryPort.existsById(storeReference)
                    || !storeRepositoryPort.isActive(storeReference)) {
                throw new RequiredWarehouseException("Un producto fisico requiere una bodega de despacho valida.");
            }
        }

        if (name == null || name.isBlank() || basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductDataException("Los datos basicos del producto son invalidos.");
        }

        Product product = new Product();
        product.setId(productId != null && !productId.isBlank() ? productId : UUID.randomUUID().toString());
        product.setName(name);
        product.setDescription(description);
        product.setBasePrice(basePrice);
        product.setSellerId(sellerId);
        product.setProductType(productType);
        product.setStoreId(productType == ProductType.Physical ? storeId : null);
        if (variants != null) {
            product.setVariants(variants);
        }
        product.setStatus(ProductStatus.published);

        return productRepositoryPort.save(product);
    }
}
