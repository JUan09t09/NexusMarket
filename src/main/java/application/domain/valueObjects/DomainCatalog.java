package application.domain.valueObjects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public abstract class DomainCatalog {
    private String code;
    private String name;
    private String description;
    
}
