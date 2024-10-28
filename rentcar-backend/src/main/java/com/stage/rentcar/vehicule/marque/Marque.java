package com.stage.rentcar.vehicule.marque;

import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "marque")
public class Marque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @OneToMany(mappedBy = "marque")
    private List<Vehicule> vehicules ;

    public MarqueRequest getDto (){
        MarqueRequest marqueRequest = new MarqueRequest() ;
        marqueRequest.setId(id);
        marqueRequest.setNom(nom);

        return marqueRequest ;
    }


}
