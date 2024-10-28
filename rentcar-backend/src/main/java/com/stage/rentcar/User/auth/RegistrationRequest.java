package com.stage.rentcar.User.auth;

import com.stage.rentcar.User.Genre;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "le nom est obligatoire")
    @NotBlank(message = "le nom est obligatoire")
    private String nom;
    @NotEmpty(message = "le prénom est obligatoire")
    @NotBlank(message = "le prénom est obligatoire")
    private String prenom;
    @NotEmpty(message = "l'email est obligatoire")
    @NotBlank(message = "l'email est obligatoire")
    @Email(message = "le format de l'email est invalide")
    private String email;
    @NotEmpty(message = "le mot de passe est obligatoire")
    @NotBlank(message = "le mot de passe est obligatoire")
    @Size(min=8, message = "le mot de passe doit etre 8 caractéres au minimum")
    private String password;

    private Set<String> roles;

    private LocalDate dateNaiss;
    private String cin;
    private String tel;
    private Genre genre ;
    String adresse;
    private Boolean archive;

}
