package application.adapters.persistence.sql.entity;

import application.domain.valueObjects.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "buyer_id")
    private String buyerId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "bill_id")
    private String billId;

    @Column(name = "shipment_id")
    private String shipmentId;

    @Column(name = "payment_reference")
    private String paymentReference;

    private boolean cancelled;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items = new ArrayList<>();
}
