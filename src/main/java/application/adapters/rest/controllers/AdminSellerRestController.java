package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.AddSalespersonRequestDTO;
import application.adapters.rest.dtos.responses.UserResponseDTO;
import application.adapters.rest.mappers.UserRestMapper;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.User;
import application.domain.ports.in.AddSalespersonUseCase;
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
@RequestMapping("/api/v1/admin/sellers")
@RequiredArgsConstructor
public class AdminSellerRestController {

    private final AddSalespersonUseCase addSalespersonUseCase;

    @PostMapping
    public ResponseEntity<UserResponseDTO> addSalesperson(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody AddSalespersonRequestDTO requestDTO) {
        User seller = addSalespersonUseCase.execute(
                principal.getUser().getIdentifier(),
                requestDTO.getTradeName(),
                requestDTO.getDocumentId(),
                requestDTO.getMail(),
                requestDTO.getWineryName(),
                requestDTO.getTypeWinery(),
                requestDTO.getWineryAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserRestMapper.toResponseDTO(seller));
    }
}
