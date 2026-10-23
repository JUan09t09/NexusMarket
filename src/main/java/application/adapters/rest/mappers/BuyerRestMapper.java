package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.BuyerResponseDTO;
import application.domain.models.Buyer;

public final class BuyerRestMapper {

    private BuyerRestMapper() {
    }

    public static BuyerResponseDTO toResponseDTO(Buyer buyer) {
        if (buyer == null) {
            return null;
        }
        BuyerResponseDTO dto = new BuyerResponseDTO();
        dto.setIdentifier(buyer.getIdentifier());
        dto.setName(buyer.getName());
        dto.setEmail(buyer.getEmail());
        dto.setRole(buyer.getRole());
        dto.setStatus(buyer.getStatus());
        dto.setMainAddress(buyer.getMainAdress());
        return dto;
    }
}
