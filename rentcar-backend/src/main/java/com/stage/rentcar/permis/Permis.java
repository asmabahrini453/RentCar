package com.stage.rentcar.permis;
import com.stage.rentcar.User.User;
import com.stage.rentcar.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permis")

public class Permis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numero;
    private LocalDate dateObtention;
    private String organisme;

    @OneToOne(mappedBy = "permis")
    private User user;
    @OneToOne(mappedBy = "permis", cascade = CascadeType.ALL)
    private Reservation reservation;
}
