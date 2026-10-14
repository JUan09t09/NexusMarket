package application.domain.ports.out;

import application.domain.models.Bill;
import java.util.Optional;

public interface BillRepositoryPort {

    Bill save(Bill bill);

    Optional<Bill> findByOrder(Bill bill);

    boolean existsByOrder(Bill bill);
}
