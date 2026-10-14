package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.RefundEntity;
import application.adapters.persistence.sql.repository.RefundJpaRepository;
import application.domain.models.Refund;
import application.domain.ports.out.RefundRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefundMySqlAdapter implements RefundRepositoryPort {
    private final RefundJpaRepository repo;

    @Override
    public Refund save(Refund refund) {
        return toDomain(repo.save(toEntity(refund)));
    }

    @Override
    public Optional<Refund> findByReturn(Refund refund) {
        if (refund == null || refund.getReturnId() == null) return Optional.empty();
        return repo.findByReturnId(refund.getReturnId()).map(this::toDomain);
    }

    private RefundEntity toEntity(Refund r) {
        if (r == null) return null;
        RefundEntity e = new RefundEntity();
        e.setId(r.getId());
        e.setReturnId(r.getReturnId());
        e.setRefundDate(r.getRefundDate());
        e.setAmount(r.getAmount());
        e.setStatus(r.getStatus());
        return e;
    }

    private Refund toDomain(RefundEntity e) {
        if (e == null) return null;
        Refund r = new Refund();
        r.setId(e.getId());
        r.setReturnId(e.getReturnId());
        r.setRefundDate(e.getRefundDate());
        r.setAmount(e.getAmount());
        r.setStatus(e.getStatus());
        return r;
    }
}
