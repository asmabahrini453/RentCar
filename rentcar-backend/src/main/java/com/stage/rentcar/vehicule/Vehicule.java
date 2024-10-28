package com.stage.rentcar.vehicule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRequest;
import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.avis.Avis;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.optionSupplementaire.Options;
import com.stage.rentcar.reservation.Reservation;
import com.stage.rentcar.vehicule.caracteristiques.Caracteristiques;
import com.stage.rentcar.vehicule.caracteristiques.CaracteristiquesRequest;
import com.stage.rentcar.vehicule.categorie.Categorie;
import com.stage.rentcar.vehicule.marque.Marque;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="vehicule")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String nom ;
    private String image ;
    private String description ;
    private String anneeCirculation ;
    private String couleur ;
    private String immatriculation ;
    private String typeAssurance ;
    private Double prixParJour ;
    private Boolean disponibilite ;
    private Double kmSortie ;
    private Double fraisAnnulation ;
    private LocalDateTime scheduledAvailabilityDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    private Boolean archive ;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    @JsonIgnore
    private Categorie categorie ;

    @ManyToOne
    @JoinColumn(name = "marque_id")
    private Marque marque ;

    @ManyToOne
    @JoinColumn(name = "caracteristiques_id")
    private Caracteristiques caracteristiques;

    @ManyToOne
    @JoinColumn(name="agence_id")
    private Agence agence ;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user ;

    @OneToMany(mappedBy = "vehicule")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "vehicule")
    private List<Avis> avis;

    public VehiculeRequest getDto() {
        VehiculeRequest response = VehiculeRequest.builder()
                .id(getId())
                .nom(getNom())
                .description(getDescription())
                .anneeCirculation(getAnneeCirculation())
                .couleur(getCouleur())
                .immatriculation(getImmatriculation())
                .typeAssurance(getTypeAssurance())
                .prixParJour(getPrixParJour())
                .disponibilite(getDisponibilite())
                .archive(getArchive())
                .fraisAnnulation(getFraisAnnulation())
                .agenceId(getAgence().getId())
                .marqueId(getMarque().getId())
                .categorie(getCategorie().getName().name())
                .userId(user.getId())
                .chefNom(user.getNom())
                .chefPrenom(user.getPrenom())
                .marqueNom(marque.getNom())
                .agenceNom(agence.getNom())
                .kmSortie(getKmSortie())
                .caracteristiques(CaracteristiquesRequest.builder()
                        .id(caracteristiques.getId())
                        .vitesse(caracteristiques.getVitesse())
                        .carburant(caracteristiques.getCarburant())
                        .radio(caracteristiques.getRadio())
                        .cd(caracteristiques.getCd())
                        .mp3(caracteristiques.getMp3())
                        .climatisation(caracteristiques.getClimatisation())
                        .packElectrique(caracteristiques.getPackElectrique())
                        .gps(caracteristiques.getGps())
                        .regulateurVitesse(caracteristiques.getRegulateurVitesse())
                        .nbrPersonnes(caracteristiques.getNbrPersonnes())
                        .chargeUtile(caracteristiques.getChargeUtile())
                        .dimensionDeChargement(caracteristiques.getDimensionDeChargement())
                        .build())
                .build();
        if (image != null) {
            try {
                byte[] imageBytes = FileUtils.readFileFromLocation(image);
                response.setImage(imageBytes);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

}
