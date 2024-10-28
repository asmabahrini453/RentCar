package com.stage.rentcar.User.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token ;
    private List<String> roles;
    private Integer userId;

}
