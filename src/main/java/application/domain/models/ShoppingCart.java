package application.domain.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ShoppingCart {
    private String id;
    private String buyerId;
    private List<Variant> items = new ArrayList<>();
}
