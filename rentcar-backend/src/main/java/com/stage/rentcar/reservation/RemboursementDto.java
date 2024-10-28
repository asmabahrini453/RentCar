package com.stage.rentcar.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemboursementDto {
    private Long reservationId;
    private Double frais;
    private RemboursementStatus remboursementStatus;
}
