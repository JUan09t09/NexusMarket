package application.domain.ports.in;

import application.domain.models.Buyer;

public interface RegisterBuyerUseCase {
    Buyer execute(String name, String documentId, String mail, String password, String mainAddress);
}
