package com.stage.rentcar.reservation;

import com.stage.rentcar.User.User;
import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.optionSupplementaire.Options;
import com.stage.rentcar.optionSupplementaire.OptionsRequest;
import com.stage.rentcar.paiment.Paiment;
import com.stage.rentcar.permis.Permis;
import com.stage.rentcar.permis.PermisRequest;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reservation")
@EntityListeners(AuditingEntityListener.class)

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private LocalDateTime dateDepart ;
    private LocalDateTime dateRetour ;
    private Integer jours ;
    private Double montantRes ;
    private Long numContrat ;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    private Boolean archive;
    private Status status ;
    private RetourStatus retourStatus;
    private RemboursementStatus remboursementStatus ;

    @OneToOne
    @JoinColumn(name = "options_id")
    private Options options;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paiment_id")
    private Paiment paiment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permis_id")
    private Permis permis;

    @ManyToOne
    @JoinColumn(name="vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="agence_id")
    private Agence agence;

    public ReservationRequest getDto() {
        ReservationRequest.ReservationRequestBuilder responseBuilder = ReservationRequest.builder()
                .id(getId())
                .dateDepart(getDateDepart())
                .dateRetour(getDateRetour())
                .jours(getJours())
                .numContrat(getNumContrat())
                .archive(getArchive())
                .status(getStatus())
                .remboursementStatus(getRemboursementStatus())
                .montantRes(getMontantRes());

        if (user != null) {
            responseBuilder
                    .userId(user.getId())
                    .clientNom(user.getNom())
                    .clientPrenom(user.getPrenom())
                    .clientCin(user.getCin())
                    .clientTel(user.getTel())
                     .dateNaiss(user.getDateNaiss())
                    .email(user.getEmail());
        }

        if (agence != null) {
            responseBuilder
                    .agenceId(agence.getId())
                    .agenceNom(agence.getNom());
            try {
                if (agence.getAgenceLogo() != null) {
                    byte[] imageBytes = FileUtils.readFileFromLocation(agence.getAgenceLogo());
                    responseBuilder.agenceLogo(imageBytes);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        if (paiment != null) {
            responseBuilder
                    .paimentId(paiment.getId())
                    .numFacture(paiment.getNumFacture())
                    .methode(paiment.getMethode());
        }

        if (vehicule != null) {
            responseBuilder
                    .vehiculeId(vehicule.getId())
                    .vehiculeNom(vehicule.getNom())
                    .vehiculeKmSorite(vehicule.getKmSortie())
                    .vehiculeDisponibilite(vehicule.getDisponibilite())
                    .immatriculation(vehicule.getImmatriculation());
            try {
                if (vehicule.getImage() != null) {
                    byte[] imageBytes = FileUtils.readFileFromLocation(vehicule.getImage());
                    responseBuilder.vehiculeImage(imageBytes);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        if (options != null) {
            responseBuilder.options(OptionsRequest.builder()
                    .id(options.getId())
                    .siegeEnfant(options.getSiegeEnfant())
                    .nbrSiegeEnfant(options.getNbrSiegeEnfant())
                    .siegeBebe(options.getSiegeBebe())
                    .nbrSiegeBebe(options.getNbrSiegeBebe())
                    .GPS(options.getGPS())
                    .nbrGPS(options.getNbrGPS())
                    .prixGPS(options.getPrixGPS())
                    .assistanceRoutiere(options.getAssistanceRoutiere())
                    .prixAssistanceRoutiere(options.getPrixAssistanceRoutiere())
                    .protectionComplete(options.getProtectionComplete())
                    .agenceLogo(options.getAgenceLogo() != null ?
                            FileUtils.readFileFromLocation(options.getAgenceLogo()) : null)
                    .build());
        }

        if (permis != null) {
            responseBuilder.permis(PermisRequest.builder()
                    .id(permis.getId())
                    .numero(permis.getNumero())
                    .dateObtention(permis.getDateObtention())
                    .organisme(permis.getOrganisme())
                    .userId(permis.getUser() != null ? permis.getUser().getId() : null)
                    .build());
        }

        return responseBuilder.build();
    }
    public HistoriqueRes getHistoriqueDto() {
        HistoriqueRes.HistoriqueResBuilder responseBuilder = HistoriqueRes.builder()
                .id(getId())
                .dateDepart(getDateDepart())
                .dateRetour(getDateRetour())
                .numContrat(getNumContrat())
                .status(getStatus())
                .montantRes(getMontantRes());
        if (agence != null) {
            responseBuilder
                    .agenceId(agence.getId())
                    .agenceNom(agence.getNom());
        }
        if (vehicule != null) {
            responseBuilder
                    .vehiculeId(vehicule.getId())
                    .vehiculeNom(vehicule.getNom())
                    .vehiculeDisponibilite(vehicule.getDisponibilite())
                    .vehiculeKmSorite(vehicule.getKmSortie());
            try {
                if (vehicule.getImage() != null) {
                    byte[] imageBytes = FileUtils.readFileFromLocation(vehicule.getImage());
                    responseBuilder.vehiculeImage(imageBytes);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        if (paiment != null) {
            responseBuilder
                    .numFacture(paiment.getNumFacture())
                    .methode(paiment.getMethode());
        }
        return responseBuilder.build();

    }
    public RetourRequest getRetourDto() {
        RetourRequest.RetourRequestBuilder responseBuilder = RetourRequest.builder()
                .id(getId())
                .dateDepart(getDateDepart())
                .dateRetour(getDateRetour())
                .numContrat(getNumContrat())
                .montantRes(getMontantRes())
                .archive(getArchive())

                .retourStatus(getRetourStatus());

        if (agence != null) {
            responseBuilder
                    .agenceId(agence.getId())
                    .agenceNom(agence.getNom());
        }
        if (vehicule != null) {
            responseBuilder
                    .vehiculeId(vehicule.getId())
                    .vehiculeNom(vehicule.getNom())
                    .vehiculeDisponibilite(vehicule.getDisponibilite());

            try {
                if (vehicule.getImage() != null) {
                    byte[] imageBytes = FileUtils.readFileFromLocation(vehicule.getImage());
                    responseBuilder.vehiculeImage(imageBytes);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        if (paiment != null) {
            responseBuilder
                    .paimentId(paiment.getId())
                    .numFacture(paiment.getNumFacture())
                    .methode(paiment.getMethode());
        }
        if (user != null) {
            responseBuilder
                    .userId(user.getId())
                    .clientNom(user.getNom())
                    .clientPrenom(user.getPrenom())
                    .clientCin(user.getCin())
                    .clientTel(user.getTel());
        }
        if (permis != null) {
            responseBuilder.permis(PermisRequest.builder()
                    .id(permis.getId())
                    .numero(permis.getNumero())
                    .userId(permis.getUser() != null ? permis.getUser().getId() : null)
                    .build());
        }
        return responseBuilder.build();

    }

}


