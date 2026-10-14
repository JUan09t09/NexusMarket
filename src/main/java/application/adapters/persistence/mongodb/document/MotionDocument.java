package application.adapters.persistence.mongodb.document;

import application.domain.valueObjects.MovementType;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory_movements")
@Getter
@Setter
public class MotionDocument {
    @Id
    private String id;
    private String inventoryId;
    private MovementType movementType;
    private Integer quantity;
    private LocalDate date;
}
