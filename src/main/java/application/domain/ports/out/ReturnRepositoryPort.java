package application.domain.ports.out;

import application.domain.models.Return;
import java.util.Optional;

public interface ReturnRepositoryPort {

    Return save(Return returnRequest);

    Optional<Return> findById(Return returnRequest);

    Optional<Return> findByOrder(Return returnRequest);

    boolean existsByOrder(Return returnRequest);
}
