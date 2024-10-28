package com.stage.rentcar.avis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvisRequest {
    private Long id ;
    private Boolean archive ;
    private Double rating ;
    private String comment ;
    private AvisType type;
    private Integer vehiculeId ;
    private String vehiculeNom ;
    private Integer userId ;
    private String nom ;
    private  byte[] image ;
    private  byte[] vehiculeImg ;
    private String prenom ;
    private LocalDateTime createdDate ;



}
