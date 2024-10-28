package com.stage.rentcar.reclamation;

import com.stage.rentcar.avis.AvisType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    private Long id ;
    private String comment ;
    private LocalDateTime createdDate;
    private Boolean archive ;
    private Integer agenceId ;
    private String agenceNom ;
    private Integer userId ;
    private String nom ;
    private String prenom ;
    private byte[] image;
    private byte[] agenceLogo;

}
