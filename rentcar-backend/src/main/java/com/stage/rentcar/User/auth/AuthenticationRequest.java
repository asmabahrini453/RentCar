package com.stage.rentcar.User.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "l'email est obligatoire")
    @NotBlank(message = "l'email est obligatoire")
    @Email(message = "le format de l'email est invalide")
    private String email;
    @NotEmpty(message = "le mot de passe est obligatoire")
    @NotBlank(message = "le mot de passe est obligatoire")
    @Size(min=8, message = "le mot de passe doit etre 8 caractéres au minimum")
    private String password;
}
