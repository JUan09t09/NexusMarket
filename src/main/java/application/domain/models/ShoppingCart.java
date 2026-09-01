package application.domain.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ShoppingCart {
    private List<Variant> items;
}
