package application.adapters.rest.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSalespersonRequestDTO {
    @NotBlank
    private String tradeName;
    @NotBlank
    private String documentId;
    @NotBlank
    @Email
    private String mail;
    @NotBlank
    private String wineryName;
    @NotBlank
    private String typeWinery;
    @NotBlank
    private String wineryAddress;
}
