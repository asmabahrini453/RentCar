package com.stage.rentcar.vehicule.caracteristiques;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaracteristiquesRequest {
    private Integer id;
    private Vitesse vitesse;
    private Carburant carburant;
    private Boolean radio;
    private Boolean cd;
    private Boolean mp3;
    private Boolean climatisation;
    private Boolean packElectrique;
    private Boolean gps;
    private Boolean regulateurVitesse;
     Integer nbrPersonnes;
     String chargeUtile;
     String dimensionDeChargement;
    public Caracteristiques toEntity() {
        Caracteristiques caracteristiques = new Caracteristiques();
        caracteristiques.setId(id);
        caracteristiques.setVitesse(this.vitesse);
        caracteristiques.setCarburant(this.carburant);
        caracteristiques.setCd(this.cd);
        caracteristiques.setMp3(this.mp3);
        caracteristiques.setGps(this.gps);
        caracteristiques.setClimatisation(this.climatisation);
        caracteristiques.setPackElectrique(this.packElectrique);
        caracteristiques.setRadio(this.radio);
        caracteristiques.setRegulateurVitesse(this.regulateurVitesse);
        caracteristiques.setNbrPersonnes(this.nbrPersonnes);
        caracteristiques.setChargeUtile(this.chargeUtile);
        caracteristiques.setDimensionDeChargement(this.dimensionDeChargement);
        return caracteristiques;
    }


}
