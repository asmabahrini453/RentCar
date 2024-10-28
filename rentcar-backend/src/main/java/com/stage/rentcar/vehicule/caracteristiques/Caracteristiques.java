package com.stage.rentcar.vehicule.caracteristiques;

import com.stage.rentcar.agence.AgenceRequest;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "caracteristiques")
public class Caracteristiques {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Vitesse vitesse;

    @Enumerated(EnumType.STRING)
    private Carburant carburant;

    private Integer nbrPersonnes;
    private String chargeUtile;
    private String dimensionDeChargement;
    private Boolean radio;
    private Boolean cd;
    private Boolean mp3;
    private Boolean climatisation;
    private Boolean packElectrique;
    private Boolean gps;
    private Boolean regulateurVitesse;

    @OneToMany(mappedBy = "caracteristiques")
    private List<Vehicule> vehicules;


    public CaracteristiquesRequest getDto() {
        CaracteristiquesRequest request = new CaracteristiquesRequest();
        request.setId(id);
        request.setVitesse(vitesse);
        request.setCarburant(carburant);
        request.setCd(cd);
        request.setMp3(mp3);
        request.setClimatisation(climatisation);
        request.setPackElectrique(packElectrique);
        request.setGps(gps);
        request.setRegulateurVitesse(regulateurVitesse);
        request.setRadio(radio);
        request.setNbrPersonnes(nbrPersonnes);
        request.setChargeUtile(chargeUtile);
        request.setDimensionDeChargement(dimensionDeChargement);
        return request;
    }






}
