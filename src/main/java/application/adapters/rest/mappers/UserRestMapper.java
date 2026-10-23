package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.UserResponseDTO;
import application.domain.models.User;

public final class UserRestMapper {

    private UserRestMapper() {
    }

    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdentifier(user.getIdentifier());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }
}
