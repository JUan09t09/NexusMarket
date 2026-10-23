package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.ProcessReturnRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Return;
import application.domain.ports.in.ProcessReturnUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistics/returns")
@RequiredArgsConstructor
public class LogisticsReturnRestController {

    private final ProcessReturnUseCase processReturnUseCase;

    @PostMapping("/{returnId}/process")
    public ResponseEntity<Return> processReturn(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable String returnId,
            @Valid @RequestBody ProcessReturnRequestDTO requestDTO) {
        Return result = processReturnUseCase.execute(
                returnId,
                principal.getUser().getIdentifier(),
                requestDTO.getStoreId());
        return ResponseEntity.ok(result);
    }
}
