package com.stage.rentcar.paiment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaimentResponse {
    private String approvalUrl;
    private String cancelUrl;
    private String successUrl;

}
