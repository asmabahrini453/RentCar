package com.stage.rentcar.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyUserStatisticsRequest {
    private LocalDate mois;
    private long count;
}
