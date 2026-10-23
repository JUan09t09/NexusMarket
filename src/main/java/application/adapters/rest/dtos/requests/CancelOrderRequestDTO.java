package application.adapters.rest.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrderRequestDTO {
    private String reason;
}
