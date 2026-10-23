package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.ChangeUserStatusRequestDTO;
import application.adapters.rest.dtos.responses.UserResponseDTO;
import application.adapters.rest.mappers.UserRestMapper;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.User;
import application.domain.ports.in.ChangeUserStatusUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserRestController {

    private final ChangeUserStatusUseCase changeUserStatusUseCase;

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponseDTO> changeStatus(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String userId,
            @Valid @RequestBody ChangeUserStatusRequestDTO requestDTO) {
        User user = changeUserStatusUseCase.execute(
                principal.getUser().getIdentifier(),
                userId,
                requestDTO.getNewStatus());
        return ResponseEntity.ok(UserRestMapper.toResponseDTO(user));
    }
}
