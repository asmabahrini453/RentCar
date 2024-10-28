package com.stage.rentcar.reservation;

import com.stage.rentcar.optionSupplementaire.Options;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MontantRequest {
     Integer vehiculeId;
     Integer jours;
     Options options;
}
