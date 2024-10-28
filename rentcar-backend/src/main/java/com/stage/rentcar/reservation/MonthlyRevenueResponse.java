package com.stage.rentcar.reservation;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyRevenueResponse {
    private LocalDate mois;
    private double total;

}
