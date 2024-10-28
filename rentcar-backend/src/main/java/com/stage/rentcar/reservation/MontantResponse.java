package com.stage.rentcar.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MontantResponse {
     Double vehiculeMontant;
     Double optionMontant;
     Double totalMontant;

}
