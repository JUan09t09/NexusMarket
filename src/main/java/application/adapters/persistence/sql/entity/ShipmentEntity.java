package application.adapters.persistence.sql.entity;

import application.domain.valueObjects.ShippingStatus;
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
@Table(name = "shipments")
@Getter
@Setter
public class ShipmentEntity {
    @Id
    private long identifier;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "buyer_id")
    private String buyerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_status")
    private ShippingStatus shippingStatus;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
}
