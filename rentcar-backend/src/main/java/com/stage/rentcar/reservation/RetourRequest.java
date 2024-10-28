package com.stage.rentcar.reservation;

import com.stage.rentcar.paiment.PayMethod;
import com.stage.rentcar.permis.PermisRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetourRequest {
    private Long id ;
    private LocalDateTime dateDepart ;
    private LocalDateTime dateRetour ;
    private Long numContrat ;
    private Boolean archive;
    private RetourStatus retourStatus ;
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
    private Integer agenceId ;
    private String agenceNom ;
    private Integer vehiculeId ;
    private String vehiculeNom ;
    private Boolean vehiculeDisponibilite ;
    private byte[] vehiculeImage ;




}
