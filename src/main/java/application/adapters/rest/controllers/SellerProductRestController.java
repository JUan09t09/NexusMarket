package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.AddProductVariantRequestDTO;
import application.adapters.rest.dtos.requests.ChangeProductStatusRequestDTO;
import application.adapters.rest.dtos.requests.PublishProductRequestDTO;
import application.adapters.rest.dtos.requests.UpdateProductRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Product;
import application.domain.ports.in.AddProductVariantUseCase;
import application.domain.ports.in.ChangeProductStatusUseCase;
import application.domain.ports.in.PublishProductUseCase;
import application.domain.ports.in.UpdateProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seller/products")
@RequiredArgsConstructor
public class SellerProductRestController {

    private final PublishProductUseCase publishProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final AddProductVariantUseCase addProductVariantUseCase;
    private final ChangeProductStatusUseCase changeProductStatusUseCase;

    @PostMapping
    public ResponseEntity<Product> publishProduct(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody PublishProductRequestDTO requestDTO) {
        Product product = publishProductUseCase.execute(
                requestDTO.getProductId(),
                principal.getUser().getIdentifier(),
                requestDTO.getProductType(),
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.getBasePrice(),
                requestDTO.getStoreId(),
                requestDTO.getVariants());
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String productId,
            @Valid @RequestBody UpdateProductRequestDTO requestDTO) {
        Product product = updateProductUseCase.execute(
                productId,
                principal.getUser().getIdentifier(),
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.getBasePrice());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{productId}/variants")
    public ResponseEntity<Product> addVariant(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String productId,
            @RequestBody AddProductVariantRequestDTO requestDTO) {
        Product product = addProductVariantUseCase.execute(
                productId,
                principal.getUser().getIdentifier(),
                requestDTO.getColor(),
                requestDTO.getSize(),
                requestDTO.getModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<Product> changeStatus(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String productId,
            @Valid @RequestBody ChangeProductStatusRequestDTO requestDTO) {
        Product product = changeProductStatusUseCase.execute(
                productId,
                principal.getUser().getIdentifier(),
                requestDTO.getNewStatus());
        return ResponseEntity.ok(product);
    }
}
