package com.stage.rentcar.optionSupplementaire;

import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "options")
@EntityListeners(AuditingEntityListener.class)
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double optionMontant;
    private String agenceLogo;

    private Boolean siegeEnfant;
    private String descSiegeEnfant;
    private Integer nbrSiegeEnfant;
    private Double prixSiegeEnfant;

    private Boolean siegeBebe;
    private String descSiegeBebe;
    private Integer nbrSiegeBebe;
    private Double prixSiegeBebe;

    private Boolean GPS;
    private String descGPS;
    private Integer nbrGPS;
    private Double prixGPS;

    private Boolean assistanceRoutiere;
    private String descAssistanceRoutiere;
    private Double prixAssistanceRoutiere;

    private Boolean protectionComplete;
    private String descProtectionComplete;
    private Double prixProtectionComplete;

    @OneToOne(mappedBy = "options", fetch = FetchType.LAZY)
    private Reservation reservation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id")
    private Agence agence;

    public OptionsRequestChef getDtoChef() {
        OptionsRequestChef optionsRequest = new OptionsRequestChef();
        optionsRequest.setId(id);

        optionsRequest.setDescSiegeEnfant(descSiegeEnfant);
        optionsRequest.setPrixSiegeEnfant(prixSiegeEnfant);

        optionsRequest.setDescSiegeBebe(descSiegeBebe);
        optionsRequest.setPrixSiegeBebe(prixSiegeBebe);

        optionsRequest.setDescGPS(descGPS);
        optionsRequest.setPrixGPS(prixGPS);

        optionsRequest.setDescAssistanceRoutiere(descAssistanceRoutiere);
        optionsRequest.setPrixAssistanceRoutiere(prixAssistanceRoutiere);

        optionsRequest.setDescProtectionComplete(descProtectionComplete);
        optionsRequest.setPrixProtectionComplete(prixProtectionComplete);

        if (agence != null) {
            optionsRequest.setAgenceId(agence.getId());
            optionsRequest.setAgenceNom(agence.getNom());
            try {
                byte[] imageBytes = FileUtils.readFileFromLocation(agence.getAgenceLogo());
                optionsRequest.setAgenceLogo(imageBytes);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return optionsRequest;
    }

    public OptionPricesRequest getDtoPrices() {
        OptionPricesRequest optionsRequest = new OptionPricesRequest();
        optionsRequest.setPrixSiegeEnfant(prixSiegeEnfant);
        optionsRequest.setPrixSiegeBebe(prixSiegeBebe);
        optionsRequest.setPrixGPS(prixGPS);
        optionsRequest.setPrixAssistanceRoutiere(prixAssistanceRoutiere);
        optionsRequest.setPrixProtectionComplete(prixProtectionComplete);
        return optionsRequest;
    }
}
