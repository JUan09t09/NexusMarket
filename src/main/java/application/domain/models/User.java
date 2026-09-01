package application.domain.models;

import lombok.Getter;
import lombok.Setter;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

@Setter
@Getter

public class User {
    private String identifier;
    private String name;
    private String email;
    private UserRole role;
    private UserStatus status;
    private String password;
}
