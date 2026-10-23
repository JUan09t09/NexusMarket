package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.CreateShipmentRequestDTO;
import application.adapters.rest.dtos.requests.UpdateShipmentStatusRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Shipment;
import application.domain.ports.in.CreateShipmentUseCase;
import application.domain.ports.in.UpdateShipmentStatusUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistics/shipments")
@RequiredArgsConstructor
public class LogisticsShipmentRestController {

    private final CreateShipmentUseCase createShipmentUseCase;
    private final UpdateShipmentStatusUseCase updateShipmentStatusUseCase;

    @PostMapping
    public ResponseEntity<Shipment> createShipment(@Valid @RequestBody CreateShipmentRequestDTO requestDTO) {
        Shipment shipment = createShipmentUseCase.execute(
                requestDTO.getOrderId(),
                requestDTO.getIdentifier(),
                requestDTO.getShippingDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }

    @PatchMapping("/{shipmentId}/status")
    public ResponseEntity<Shipment> updateStatus(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable long shipmentId,
            @Valid @RequestBody UpdateShipmentStatusRequestDTO requestDTO) {
        Shipment shipment = updateShipmentStatusUseCase.execute(
                shipmentId,
                principal.getUser().getIdentifier(),
                requestDTO.getNewStatus(),
                requestDTO.getDeliveryDate());
        return ResponseEntity.ok(shipment);
    }
}
