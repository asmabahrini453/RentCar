package com.stage.rentcar.vehicule.marque;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarqueRequest {
    Integer id ;
    String nom ;
    byte[] image ;
}
