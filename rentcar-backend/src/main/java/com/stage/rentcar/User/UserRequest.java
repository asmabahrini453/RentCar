package com.stage.rentcar.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Integer id ;
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

    @Size(min=8, message = "le mot de passe doit etre 8 caractéres au minimum")
    private String password;
    private LocalDate dateNaiss;
    private Boolean accountLocked;
    private Boolean enabled ;

    private String role;

    private String cin;
    private String tel;
    private byte[] image;
    private Genre genre ;
    private Boolean archive;
     String adresse;

}
