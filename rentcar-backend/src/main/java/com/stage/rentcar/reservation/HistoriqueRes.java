package com.stage.rentcar.reservation;

import com.stage.rentcar.paiment.PayMethod;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRes {
    private Long id ;
    private LocalDateTime dateDepart ;
    private LocalDateTime dateRetour ;
    private Long numContrat ;
    private Status status ;
    private Double montantRes ;
    private Long numFacture ;
    private PayMethod methode ;
    private Integer agenceId ;
    private String agenceNom ;
    private Integer vehiculeId ;
    private String vehiculeNom ;
    private byte[] vehiculeImage ;
    private Double vehiculeKmSorite ;
    private Boolean vehiculeDisponibilite ;







}
