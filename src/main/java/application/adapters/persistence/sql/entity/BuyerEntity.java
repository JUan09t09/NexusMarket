package application.adapters.persistence.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "identifier")
public class BuyerEntity extends UserEntity {
    @Column(name = "main_address")
    private String mainAdress;

    @Column(name = "additional_address")
    private String additionalAdress;

    @Column(name = "comersial_status")
    private String comersialStatus;
}
