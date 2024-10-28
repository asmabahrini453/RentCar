package com.stage.rentcar.permis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermisRequest {
    private Integer id;
    private String numero;
    private LocalDate dateObtention;
    private String organisme;
    private Integer userId;

}
