package application.adapters.persistence.sql.entity;

import application.domain.valueObjects.RefundStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refunds")
@Getter
@Setter
public class RefundEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "return_id")
    private String returnId;

    @Column(name = "refund_date")
    private LocalDate refundDate;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;
}
