package application.domain.ports.out;

import application.domain.models.Motion;

public interface InventoryMovementRepositoryPort {

    Motion save(Motion movement);
}
