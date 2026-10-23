package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.AdjustInventoryRequestDTO;
import application.adapters.rest.dtos.requests.ConfirmSaleOutputRequestDTO;
import application.adapters.rest.dtos.requests.RegisterInventoryEntryRequestDTO;
import application.adapters.rest.dtos.requests.ReleaseInventoryReservationRequestDTO;
import application.adapters.rest.dtos.requests.ReserveInventoryRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Inventario;
import application.domain.models.ReserveResult;
import application.domain.ports.in.AdjustInventoryUseCase;
import application.domain.ports.in.ConfirmSaleOutputUseCase;
import application.domain.ports.in.RegisterInventoryEntryUseCase;
import application.domain.ports.in.ReleaseInventoryReservationUseCase;
import application.domain.ports.in.ReserveInventoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistics/inventory")
@RequiredArgsConstructor
public class LogisticsInventoryRestController {

    private final ReserveInventoryUseCase reserveInventoryUseCase;
    private final ReleaseInventoryReservationUseCase releaseInventoryReservationUseCase;
    private final ConfirmSaleOutputUseCase confirmSaleOutputUseCase;
    private final RegisterInventoryEntryUseCase registerInventoryEntryUseCase;
    private final AdjustInventoryUseCase adjustInventoryUseCase;

    @PostMapping("/reserve")
    public ResponseEntity<ReserveResult> reserve(@Valid @RequestBody ReserveInventoryRequestDTO requestDTO) {
        ReserveResult result = reserveInventoryUseCase.execute(
                requestDTO.getProductId(),
                requestDTO.getStoreId(),
                requestDTO.getQuantityReserve());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/release")
    public ResponseEntity<Inventario> release(@Valid @RequestBody ReleaseInventoryReservationRequestDTO requestDTO) {
        Inventario inventory = releaseInventoryReservationUseCase.execute(
                requestDTO.getProductId(),
                requestDTO.getStoreId(),
                requestDTO.getQuantity());
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/sale-output")
    public ResponseEntity<Inventario> confirmSaleOutput(@Valid @RequestBody ConfirmSaleOutputRequestDTO requestDTO) {
        Inventario inventory = confirmSaleOutputUseCase.execute(
                requestDTO.getProductId(),
                requestDTO.getStoreId(),
                requestDTO.getQuantity(),
                requestDTO.getOrderId());
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/entry")
    public ResponseEntity<Inventario> registerEntry(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody RegisterInventoryEntryRequestDTO requestDTO) {
        Inventario inventory = registerInventoryEntryUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getProductId(),
                requestDTO.getStoreId(),
                requestDTO.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @PostMapping("/adjust")
    public ResponseEntity<Inventario> adjust(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody AdjustInventoryRequestDTO requestDTO) {
        Inventario inventory = adjustInventoryUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getProductId(),
                requestDTO.getStoreId(),
                requestDTO.getNewAmount(),
                requestDTO.getReason());
        return ResponseEntity.ok(inventory);
    }
}
