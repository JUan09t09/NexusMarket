package application.domain.models;

import java.time.LocalDate;
import application.domain.valueObjects.MovementType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Motion {
    private String id;
    private String inventoryId;
    private MovementType movementType;
    private int quantity;
    private LocalDate date;

}
