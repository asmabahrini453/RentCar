package com.stage.rentcar.agence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgenceRequest {
     Integer id;
     Integer userId;
     String chefNom;
     String chefPrenom;
     String chefTel;
     String nom ;
     LocalDate dteCreation;
      byte[] agenceLogo;
     Boolean archive;
      LocalTime heureOuverture;
      LocalTime heureFermeture;
      String localisation ;
}
