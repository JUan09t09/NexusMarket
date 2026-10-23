package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.CreateStoreRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Store;
import application.domain.ports.in.CreateStoreUseCase;
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
@RequestMapping("/api/v1/admin/stores")
@RequiredArgsConstructor
public class AdminStoreRestController {

    private final CreateStoreUseCase createStoreUseCase;

    @PostMapping
    public ResponseEntity<Store> createStore(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody CreateStoreRequestDTO requestDTO) {
        Store store = createStoreUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getSellerId(),
                requestDTO.getWarehouseName(),
                requestDTO.getWarehouseType(),
                requestDTO.getWarehouseAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(store);
    }
}
