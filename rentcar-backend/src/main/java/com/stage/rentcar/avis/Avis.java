package com.stage.rentcar.avis;

import com.stage.rentcar.User.User;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.vehicule.Vehicule;
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
@Table(name="avis")
@EntityListeners(AuditingEntityListener.class)
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Double rating ;
    private String comment ;
    @Enumerated(EnumType.STRING)
    private AvisType type;
    private Boolean archive ;

    @ManyToOne
    @JoinColumn(name = "vehicule_id" , nullable = true) //nullable = true: khtr ynjm l'avis lel vehicule =
                                                        // null mch bl dharoura yaati avis
    private Vehicule vehicule ;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) //kol avis teb3a le client kaaleh =false
    private User user;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public AvisRequest getDto(){
        AvisRequest avis = new AvisRequest();
        avis.setId(id);
        avis.setComment(comment);
        avis.setRating(rating);
        avis.setArchive(archive);
        avis.setType(type);
        avis.setCreatedDate(createdDate);
        if(vehicule !=null){
            avis.setVehiculeId(vehicule.getId());
            avis.setVehiculeNom(vehicule.getNom());
            if (vehicule.getImage() != null) {
                try {
                    byte[] imageBytes = FileUtils.readFileFromLocation(vehicule.getImage());
                    avis.setVehiculeImg(imageBytes);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
         if(user !=null){
            avis.setUserId(user.getId());
            avis.setNom(user.getNom());
            avis.setPrenom(user.getPrenom());
             if (user.getImage() != null) {
                 try {
                     byte[] imageBytes = FileUtils.readFileFromLocation(user.getImage());
                     avis.setImage(imageBytes);
                 } catch (IndexOutOfBoundsException e) {
                     e.printStackTrace();
                 }
             }
        }
         return avis;
    }


}
