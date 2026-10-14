package application.domain.models;

import java.util.ArrayList;
import java.util.List;
import application.domain.valueObjects.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Order {
    private String id;
    private String buyerId;
    private String storeId;
    private String billId;
    private String shipmentId;
    private String paymentReference;
    private boolean cancelled;
    private List<Variant> items = new ArrayList<>();
    private OrderStatus orderStatus;

}
