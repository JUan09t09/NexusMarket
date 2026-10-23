package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.CancelOrderRequestDTO;
import application.adapters.rest.dtos.requests.ConfirmOrderPaymentRequestDTO;
import application.adapters.rest.dtos.requests.CreateOrderRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Order;
import application.domain.ports.in.CancelOrderUseCase;
import application.domain.ports.in.ConfirmOrderPaymentUseCase;
import application.domain.ports.in.CreateOrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buyer/orders")
@RequiredArgsConstructor
public class BuyerOrderRestController {

    private final CreateOrderUseCase createOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final ConfirmOrderPaymentUseCase confirmOrderPaymentUseCase;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody CreateOrderRequestDTO requestDTO) {
        Order order = createOrderUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getCartId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String orderId,
            @RequestBody(required = false) CancelOrderRequestDTO requestDTO) {
        String reason = requestDTO == null ? null : requestDTO.getReason();
        Order order = cancelOrderUseCase.execute(
                orderId,
                principal.getUser().getIdentifier(),
                reason);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Order> confirmPayment(
            @PathVariable String orderId,
            @Valid @RequestBody ConfirmOrderPaymentRequestDTO requestDTO) {
        Order order = confirmOrderPaymentUseCase.execute(orderId, requestDTO.getPaymentReference());
        return ResponseEntity.ok(order);
    }
}
