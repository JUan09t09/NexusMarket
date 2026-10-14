package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.ReturnEntity;
import application.adapters.persistence.sql.repository.ReturnJpaRepository;
import application.domain.models.Return;
import application.domain.ports.out.ReturnRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReturnMySqlAdapter implements ReturnRepositoryPort {
    private final ReturnJpaRepository repo;

    @Override
    public Return save(Return returnRequest) {
        return toDomain(repo.save(toEntity(returnRequest)));
    }

    @Override
    public Optional<Return> findById(Return returnRequest) {
        if (returnRequest == null || returnRequest.getId() == null) return Optional.empty();
        return repo.findById(returnRequest.getId()).map(this::toDomain);
    }

    @Override
    public Optional<Return> findByOrder(Return returnRequest) {
        if (returnRequest == null || returnRequest.getOrderId() == null) return Optional.empty();
        return repo.findByOrderId(returnRequest.getOrderId()).map(this::toDomain);
    }

    @Override
    public boolean existsByOrder(Return returnRequest) {
        if (returnRequest == null || returnRequest.getOrderId() == null) return false;
        return repo.existsByOrderId(returnRequest.getOrderId());
    }

    private ReturnEntity toEntity(Return r) {
        if (r == null) return null;
        ReturnEntity e = new ReturnEntity();
        e.setId(r.getId());
        e.setOrderId(r.getOrderId());
        e.setBuyerId(r.getBuyerId());
        e.setProductId(r.getProductId());
        e.setQuantity(r.getQuantity());
        e.setReturnDate(r.getReturnDate());
        e.setReason(r.getReason());
        e.setReturnStatus(r.getReturnStatus());
        return e;
    }

    private Return toDomain(ReturnEntity e) {
        if (e == null) return null;
        Return r = new Return();
        r.setId(e.getId());
        r.setOrderId(e.getOrderId());
        r.setBuyerId(e.getBuyerId());
        r.setProductId(e.getProductId());
        r.setQuantity(e.getQuantity());
        r.setReturnDate(e.getReturnDate());
        r.setReason(e.getReason());
        r.setReturnStatus(e.getReturnStatus());
        return r;
    }
}
