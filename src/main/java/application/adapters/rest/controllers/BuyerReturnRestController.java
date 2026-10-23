package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.RequestReturnRequestDTO;
import application.adapters.security.AuthenticatedUserPrincipal;
import application.domain.models.Return;
import application.domain.ports.in.RequestReturnUseCase;
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
@RequestMapping("/api/v1/buyer/returns")
@RequiredArgsConstructor
public class BuyerReturnRestController {

    private final RequestReturnUseCase requestReturnUseCase;

    @PostMapping
    public ResponseEntity<Return> requestReturn(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody RequestReturnRequestDTO requestDTO) {
        Return result = requestReturnUseCase.execute(
                requestDTO.getOrderId(),
                principal.getUser().getIdentifier(),
                requestDTO.getReason(),
                requestDTO.getReturnDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
