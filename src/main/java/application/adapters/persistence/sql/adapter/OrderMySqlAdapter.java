package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.OrderEntity;
import application.adapters.persistence.sql.entity.OrderItemEntity;
import application.adapters.persistence.sql.repository.OrderJpaRepository;
import application.domain.models.Order;
import application.domain.models.Variant;
import application.domain.ports.out.OrderRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderMySqlAdapter implements OrderRepositoryPort {
    private final OrderJpaRepository repo;

    @Override
    public Order save(Order order) {
        return toDomain(repo.save(toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Order order) {
        if (order == null || order.getId() == null) return Optional.empty();
        return repo.findById(order.getId()).map(this::toDomain);
    }

    @Override
    public Optional<Order> findByIdAndBuyer(Order order) {
        if (order == null || order.getId() == null || order.getBuyerId() == null) return Optional.empty();
        return repo.findByIdAndBuyerId(order.getId(), order.getBuyerId()).map(this::toDomain);
    }

    private OrderEntity toEntity(Order o) {
        if (o == null) return null;
        OrderEntity e = new OrderEntity();
        e.setId(o.getId());
        e.setBuyerId(o.getBuyerId());
        e.setStoreId(o.getStoreId());
        e.setBillId(o.getBillId());
        e.setShipmentId(o.getShipmentId());
        e.setPaymentReference(o.getPaymentReference());
        e.setCancelled(o.isCancelled());
        e.setOrderStatus(o.getOrderStatus());
        if (o.getItems() != null) {
            for (Variant v : o.getItems()) {
                OrderItemEntity ie = new OrderItemEntity();
                ie.setId(v.getId());
                ie.setOrderId(o.getId());
                ie.setProductId(v.getProductId());
                ie.setVariantId(v.getId());
                ie.setColor(v.getColor());
                ie.setSize(v.getSize());
                ie.setModel(v.getModel());
                ie.setQuantity(v.getQuantity());
                ie.setPrice(v.getPrice());
                ie.setOrder(e);
                e.getItems().add(ie);
            }
        }
        return e;
    }

    private Order toDomain(OrderEntity e) {
        if (e == null) return null;
        Order o = new Order();
        o.setId(e.getId());
        o.setBuyerId(e.getBuyerId());
        o.setStoreId(e.getStoreId());
        o.setBillId(e.getBillId());
        o.setShipmentId(e.getShipmentId());
        o.setPaymentReference(e.getPaymentReference());
        o.setCancelled(e.isCancelled());
        o.setOrderStatus(e.getOrderStatus());
        if (e.getItems() != null) {
            for (OrderItemEntity ie : e.getItems()) {
                Variant v = new Variant();
                v.setId(ie.getVariantId());
                v.setProductId(ie.getProductId());
                v.setColor(ie.getColor());
                v.setSize(ie.getSize());
                v.setModel(ie.getModel());
                v.setQuantity(ie.getQuantity());
                v.setPrice(ie.getPrice());
                o.getItems().add(v);
            }
        }
        return o;
    }
}
