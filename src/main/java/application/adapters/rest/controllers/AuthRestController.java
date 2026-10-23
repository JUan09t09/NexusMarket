package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.LoginRequestDTO;
import application.adapters.rest.dtos.requests.RegisterBuyerRequestDTO;
import application.adapters.rest.dtos.responses.BuyerResponseDTO;
import application.adapters.rest.dtos.responses.LoginResponseDTO;
import application.adapters.rest.mappers.BuyerRestMapper;
import application.adapters.rest.mappers.UserRestMapper;
import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.in.AuthenticateUserUseCase;
import application.domain.ports.in.RegisterBuyerUseCase;
import application.domain.ports.out.JwtServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegisterBuyerUseCase registerBuyerUseCase;
    private final JwtServicePort jwtServicePort;
    private final long expirationMs;

    public AuthRestController(
            AuthenticateUserUseCase authenticateUserUseCase,
            RegisterBuyerUseCase registerBuyerUseCase,
            JwtServicePort jwtServicePort,
            @Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.registerBuyerUseCase = registerBuyerUseCase;
        this.jwtServicePort = jwtServicePort;
        this.expirationMs = expirationMs;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        User user = authenticateUserUseCase.execute(requestDTO.getMail(), requestDTO.getPassword());
        String token = jwtServicePort.generateToken(user);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(expirationMs / 1000);
        response.setUser(UserRestMapper.toResponseDTO(user));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<BuyerResponseDTO> registerBuyer(@Valid @RequestBody RegisterBuyerRequestDTO requestDTO) {
        Buyer buyer = registerBuyerUseCase.execute(
                requestDTO.getName(),
                requestDTO.getDocumentId(),
                requestDTO.getMail(),
                requestDTO.getPassword(),
                requestDTO.getMainAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerRestMapper.toResponseDTO(buyer));
    }
}
