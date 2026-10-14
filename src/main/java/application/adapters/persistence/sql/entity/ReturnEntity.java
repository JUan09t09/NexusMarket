package application.adapters.persistence.sql.entity;

import application.domain.valueObjects.ReturnStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "returns")
@Getter
@Setter
public class ReturnEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "buyer_id")
    private String buyerId;

    private String reason;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status")
    private ReturnStatus returnStatus;

    @Column(name = "product_id")
    private String productId;

    private int quantity;
}
