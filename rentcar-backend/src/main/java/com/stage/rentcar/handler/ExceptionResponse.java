package com.stage.rentcar.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

//it is a wrapper class for our exception
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//include only the non-empty attributes
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
    private Integer businessErrorCode ;
    private String businessErrorDescription ;
    private String error;
    private Set<String> validationErrors ;
    private Map<String ,String> errors ;





}
