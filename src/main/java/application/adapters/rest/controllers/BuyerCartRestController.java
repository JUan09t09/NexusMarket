package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.AddItemToCartRequestDTO;
import application.adapters.rest.dtos.requests.ConfirmCartRequestDTO;
import application.adapters.rest.dtos.requests.RemoveItemFromCartRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Order;
import application.domain.models.ShoppingCart;
import application.domain.ports.in.AddItemToCartUseCase;
import application.domain.ports.in.ConfirmCartUseCase;
import application.domain.ports.in.RemoveItemFromCartUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buyer/cart")
@RequiredArgsConstructor
public class BuyerCartRestController {

    private final AddItemToCartUseCase addItemToCartUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final ConfirmCartUseCase confirmCartUseCase;

    @PostMapping("/items")
    public ResponseEntity<ShoppingCart> addItem(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody AddItemToCartRequestDTO requestDTO) {
        ShoppingCart cart = addItemToCartUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getProductId(),
                requestDTO.getVariantId(),
                requestDTO.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items")
    public ResponseEntity<ShoppingCart> removeItem(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody RemoveItemFromCartRequestDTO requestDTO) {
        ShoppingCart cart = removeItemFromCartUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getVariantId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Order> confirmCart(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody ConfirmCartRequestDTO requestDTO) {
        Order order = confirmCartUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getCartId());
        return ResponseEntity.ok(order);
    }
}
