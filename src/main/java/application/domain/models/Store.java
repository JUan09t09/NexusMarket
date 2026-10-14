package application.domain.models;

import application.domain.valueObjects.WarehouseType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Store {
    private String id;
    private String name;
    private String address;
    private String sellerId;
    private boolean active;
    private WarehouseType warehouseType;
}
