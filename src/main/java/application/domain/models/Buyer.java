package application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Buyer extends User{
    private String MainAdress;
    private String AdditionalAdress;
    private String ComersialStatus;
    private User User;

}
