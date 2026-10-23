package application.adapters.rest.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private String tokenType;
    private long expiresIn;
    private UserResponseDTO user;
}
