package application.adapters.rest.dtos.responses;

import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String identifier;
    private String name;
    private String email;
    private UserRole role;
    private UserStatus status;
}
