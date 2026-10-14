package application.adapters.persistence.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bills")
@Getter
@Setter
public class BillEntity {
    @Id
    @Column(name = "invoice_id", nullable = false, unique = true)
    private String invoiceId;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}
