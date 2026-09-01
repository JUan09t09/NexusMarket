package application.domain.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import application.domain.valueObjects.ReturnStatus;

@Getter
@Setter

public class Return {
    private LocalDate returnDate;
    private String reason;
    private ReturnStatus returnStatus;

}
