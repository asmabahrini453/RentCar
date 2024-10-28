package com.stage.rentcar.optionSupplementaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionsRequest {
    private Integer id ;
    private Double  optionMontant ;


    private Boolean siegeEnfant ;
    private String descSiegeEnfant ;
    private Integer nbrSiegeEnfant ;
    private Double prixSiegeEnfant ;

    private Boolean siegeBebe ;
    private String descSiegeBebe ;
    private Integer nbrSiegeBebe ;
    private Double prixSiegeBebe;

    private Boolean GPS ;
    private String descGPS ;
    private Integer nbrGPS ;
    private Double prixGPS;

    private Boolean assistanceRoutiere ;
    private String descAssistanceRoutiere ;
    private Double prixAssistanceRoutiere;

    private Boolean protectionComplete ;
    private String descProtectionComplete ;
    private Double prixProtectionComplete;

    private Integer agenceId ;
    private String agenceNom ;
    private byte[] agenceLogo ;

}
