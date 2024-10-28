package com.stage.rentcar.vehicule.categorie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorie")
public class Categorie {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ECategorie name;

    @OneToMany(mappedBy = "categorie")
    @JsonIgnore
    private List<Vehicule> vehicules ;

    public Categorie(ECategorie name) {
        this.name = name;
    }


}
