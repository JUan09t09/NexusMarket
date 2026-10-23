package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterBuyerRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String documentId;
    @NotBlank
    @Email
    private String mail;
    @NotBlank
    private String password;
    @NotBlank
    private String mainAddress;
}
