package com.stage.rentcar.reclamation;

import com.stage.rentcar.User.User;
import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.avis.AvisRequest;
import com.stage.rentcar.file.FileUtils;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reclamation")
@EntityListeners(AuditingEntityListener.class)
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String comment ;
    private Boolean archive ;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "agence_id", nullable = false)
    private Agence agence;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public ReclamationRequest getDto(){
        ReclamationRequest reclamation = new ReclamationRequest();
        reclamation.setId(id);
        reclamation.setComment(comment);
        reclamation.setArchive(archive);
        reclamation.setCreatedDate(createdDate);

        if(agence !=null){
            reclamation.setAgenceId(agence.getId());
            reclamation.setAgenceNom(agence.getNom());
            if (agence.getAgenceLogo() != null) {
                try {
                    byte[] imageBytes = FileUtils.readFileFromLocation(agence.getAgenceLogo());
                    reclamation.setAgenceLogo(imageBytes);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        if(user !=null){
            reclamation.setUserId(user.getId());
            reclamation.setNom(user.getNom());
            reclamation.setPrenom(user.getPrenom());
            if(user.getImage() != null){
                byte[] imageBytes = FileUtils.readFileFromLocation(user.getImage());
                reclamation.setImage(imageBytes);
            }
        }
        return reclamation;
    }



}
