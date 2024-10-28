package com.stage.rentcar.vehicule;

import com.stage.rentcar.vehicule.caracteristiques.CaracteristiquesRequest;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehiculeRequest {
     Integer id ;
    private String nom ;
    private String description ;
    private String anneeCirculation ;
    private String couleur ;
    private String immatriculation ;
    private String typeAssurance ;
    private Double prixParJour ;
    private Boolean disponibilite ;
    Boolean archive ;
     Integer agenceId ;
    private Double kmSortie ;
    private Double fraisAnnulation ;

    Integer marqueId ;
    String categorie;
    Integer userId;
    String chefNom;
    String chefPrenom;
    String marqueNom;
    String agenceNom;
     CaracteristiquesRequest caracteristiques;
    private byte[] image;

}
