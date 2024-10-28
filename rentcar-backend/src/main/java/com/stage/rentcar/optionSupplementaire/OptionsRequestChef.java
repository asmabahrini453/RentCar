package com.stage.rentcar.optionSupplementaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OptionsRequestChef {
     Integer id ;
    private String descSiegeEnfant ;
    private Double prixSiegeEnfant ;

    private String descSiegeBebe ;
    private Double prixSiegeBebe;

    private String descGPS ;
    private Double prixGPS;

    private String descAssistanceRoutiere ;
    private Double prixAssistanceRoutiere;

    private String descProtectionComplete ;
    private Double prixProtectionComplete;

    private Integer agenceId ;
    private String agenceNom ;
    private byte[] agenceLogo ;

}
