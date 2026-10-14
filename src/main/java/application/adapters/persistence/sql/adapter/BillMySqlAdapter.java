package application.adapters.persistence.sql.adapter;

import application.adapters.persistence.sql.entity.BillEntity;
import application.adapters.persistence.sql.repository.BillJpaRepository;
import application.domain.models.Bill;
import application.domain.ports.out.BillRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BillMySqlAdapter implements BillRepositoryPort {
    private final BillJpaRepository repo;

    @Override
    public Bill save(Bill bill) {
        return toDomain(repo.save(toEntity(bill)));
    }

    @Override
    public Optional<Bill> findByOrder(Bill bill) {
        if (bill == null || bill.getOrderId() == null) return Optional.empty();
        return repo.findByOrderId(bill.getOrderId()).map(this::toDomain);
    }

    @Override
    public boolean existsByOrder(Bill bill) {
        if (bill == null || bill.getOrderId() == null) return false;
        return repo.existsByOrderId(bill.getOrderId());
    }

    private BillEntity toEntity(Bill b) {
        if (b == null) return null;
        BillEntity e = new BillEntity();
        e.setInvoiceId(b.getInvoiceId());
        e.setOrderId(b.getOrderId());
        e.setInvoiceDate(b.getInvoiceDate());
        e.setTotalAmount(b.getTotalAmount());
        return e;
    }

    private Bill toDomain(BillEntity e) {
        if (e == null) return null;
        Bill b = new Bill();
        b.setInvoiceId(e.getInvoiceId());
        b.setOrderId(e.getOrderId());
        b.setInvoiceDate(e.getInvoiceDate());
        b.setTotalAmount(e.getTotalAmount());
        return b;
    }
}
