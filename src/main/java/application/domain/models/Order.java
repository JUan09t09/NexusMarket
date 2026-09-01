package application.domain.models;

import lombok.Getter;
import lombok.Setter;
import application.domain.valueObjects.OrderStatus;

@Getter
@Setter

public class Order {
    private OrderStatus orderStatus;

}
