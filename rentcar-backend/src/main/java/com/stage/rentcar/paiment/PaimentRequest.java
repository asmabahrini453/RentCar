package com.stage.rentcar.paiment;

import lombok.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaimentRequest {
    private Long id ;
    private Long numFacture ;
    private Double montantTotal ;
    private Double tax ;
    private LocalDateTime dateFac ;
    private PayMethod  methode;
    private Long reservationId ;

}
