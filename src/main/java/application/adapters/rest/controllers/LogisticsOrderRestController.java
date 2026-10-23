package application.adapters.rest.controllers;

import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Order;
import application.domain.ports.in.DispatchOrderUseCase;
import application.domain.ports.in.FinalizeOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistics/orders")
@RequiredArgsConstructor
public class LogisticsOrderRestController {

    private final DispatchOrderUseCase dispatchOrderUseCase;
    private final FinalizeOrderUseCase finalizeOrderUseCase;

    @PostMapping("/{orderId}/dispatch")
    public ResponseEntity<Order> dispatch(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String orderId) {
        Order order = dispatchOrderUseCase.execute(orderId, principal.getUser().getIdentifier());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/finalize")
    public ResponseEntity<Order> finalizeOrder(@PathVariable String orderId) {
        Order order = finalizeOrderUseCase.execute(orderId);
        return ResponseEntity.ok(order);
    }
}
