package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.ProcessRefundRequestDTO;
import application.domain.models.Bill;
import application.domain.models.Refund;
import application.domain.ports.in.GenerateBillUseCase;
import application.domain.ports.in.ProcessRefundUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminFinanceRestController {

    private final GenerateBillUseCase generateBillUseCase;
    private final ProcessRefundUseCase processRefundUseCase;

    @PostMapping("/billing/orders/{orderId}/bill")
    public ResponseEntity<Bill> generateBill(@PathVariable String orderId) {
        Bill bill = generateBillUseCase.execute(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(bill);
    }

    @PostMapping("/refunds")
    public ResponseEntity<Refund> processRefund(@Valid @RequestBody ProcessRefundRequestDTO requestDTO) {
        Refund refund = processRefundUseCase.execute(
                requestDTO.getReturnId(),
                requestDTO.getAmount(),
                requestDTO.getRefundDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(refund);
    }
}
