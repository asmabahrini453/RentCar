package com.stage.rentcar.vehicule;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleCategoryStatistics {
    private String categorie;
    private Long count;
    private Double pourcentage;
}
