package application.adapters.rest.dtos.requests;

import application.domain.valueObjects.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserStatusRequestDTO {
    @NotNull
    private UserStatus newStatus;
}
