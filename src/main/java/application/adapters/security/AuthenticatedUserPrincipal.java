package application.adapters.security;

import application.domain.models.User;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUserPrincipal implements UserDetails {

    private final User user;

    public AuthenticatedUserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static String authorityOf(UserRole role) {
        if (role == null) {
            return "ROLE_USER";
        }
        return switch (role) {
            case Buyer -> "ROLE_BUYER";
            case Seller -> "ROLE_SELLER";
            case LogisticOperator -> "ROLE_LOGISTIC_OPERATOR";
            case Administrator -> "ROLE_ADMINISTRATOR";
        };
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorityOf(user.getRole())));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == UserStatus.Active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.Active;
    }
}
