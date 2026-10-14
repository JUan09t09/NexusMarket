package application.domain.ports.out;

import application.domain.models.User;

public interface PasswordEncoderPort {

    String encode(User user, String rawPassword);

    boolean matches(User user, String rawPassword);
}
