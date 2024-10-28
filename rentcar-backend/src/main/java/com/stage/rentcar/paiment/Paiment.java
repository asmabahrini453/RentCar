package com.stage.rentcar.paiment;

import com.stage.rentcar.reservation.Reservation;
import com.stage.rentcar.reservation.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paiment")
public class Paiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long numFacture ;
    private Double montantTotal ;
    private Double tax ;
    private LocalDateTime dateFac ;
    private PayMethod methode;
    private Status payStatus;


    @OneToOne(mappedBy = "paiment", cascade = CascadeType.ALL)
    private Reservation reservation;

}
