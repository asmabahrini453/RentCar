package com.stage.rentcar.optionSupplementaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionPricesRequest {
    private Double prixSiegeEnfant ;

    private Double prixSiegeBebe;

    private Double prixGPS;

    private Double prixAssistanceRoutiere;

    private Double prixProtectionComplete;

}
