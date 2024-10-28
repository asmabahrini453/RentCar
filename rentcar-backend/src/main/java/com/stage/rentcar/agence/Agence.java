package com.stage.rentcar.agence;

import com.stage.rentcar.User.User;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.optionSupplementaire.Options;
import com.stage.rentcar.reclamation.Reclamation;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="agence")
@EntityListeners(AuditingEntityListener.class)
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String nom ;
    private LocalDate dteCreation;
    private LocalTime heureOuverture;
    private LocalTime heureFermeture;
    @Column(length = 1000)
    private String localisation;
    private String agenceLogo;
    private Boolean archive;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "agence")
    private List<Vehicule> vehicules;

    @OneToOne(mappedBy = "agence", fetch = FetchType.LAZY)
    private Options options;

    @OneToMany(mappedBy = "agence")
    private List<Reclamation> reclamations;

    public AgenceRequest getDto() {
        AgenceRequest agenceRequest = new AgenceRequest();
        agenceRequest.setId(id);
        agenceRequest.setNom(nom);
        agenceRequest.setDteCreation(dteCreation);
        agenceRequest.setHeureOuverture(heureOuverture);
        agenceRequest.setHeureFermeture(heureFermeture);
        agenceRequest.setLocalisation(localisation);
        agenceRequest.setArchive(archive);
        if (user != null) {
            agenceRequest.setUserId(user.getId());
            agenceRequest.setChefNom(user.getNom());
            agenceRequest.setChefPrenom(user.getPrenom());
            agenceRequest.setChefTel(user.getTel());
        }
        if (agenceLogo != null) {
            try {
                byte[] imageBytes = FileUtils.readFileFromLocation(agenceLogo);
                agenceRequest.setAgenceLogo(imageBytes);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return agenceRequest;
    }



}
