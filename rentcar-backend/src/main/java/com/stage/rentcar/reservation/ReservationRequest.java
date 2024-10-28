package com.stage.rentcar.reservation;

import com.stage.rentcar.optionSupplementaire.OptionsRequest;
import com.stage.rentcar.paiment.PayMethod;
import com.stage.rentcar.permis.PermisRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private Long id ;
    private LocalDateTime dateDepart ;
    private LocalDateTime dateRetour ;
    private Integer jours ;
    private Long numContrat ;
    private Boolean archive;
    private Status status ;
    private RemboursementStatus remboursementStatus;
    private Double montantRes ;
    private Integer userId;
    private String clientNom;
    private String clientPrenom;
    private String clientCin;
    private String clientTel;
    private PermisRequest permis;
    private Long paimentId ;
    private Long numFacture ;
    private PayMethod methode ;
    private OptionsRequest options;
    private Integer agenceId ;
    private String agenceNom ;
    private byte[] agenceLogo ;
    private Integer vehiculeId ;
    private String vehiculeNom ;
    private byte[] vehiculeImage ;
    private Double vehiculeKmSorite ;
    private Boolean vehiculeDisponibilite ;

    private String email;
    private LocalDate dateNaiss;
    private String immatriculation;
    private String marqueNom;

}

