package application.domain.ports.out;

import application.domain.models.User;

public interface JwtServicePort {

    String generateToken(User user);

    boolean isValid(String token);

    String extractIdentifier(String token);
}
