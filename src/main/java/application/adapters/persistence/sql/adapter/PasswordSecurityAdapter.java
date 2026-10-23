package application.adapters.persistence.sql.adapter;

import application.domain.models.User;
import application.domain.ports.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordSecurityAdapter implements PasswordEncoderPort {
    private final PasswordEncoder encoder;

    @Override
    public String encode(User user, String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(User user, String rawPassword) {
        if (user == null || user.getPassword() == null || rawPassword == null) return false;
        return encoder.matches(rawPassword, user.getPassword());
    }
}
