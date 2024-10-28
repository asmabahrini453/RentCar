package com.stage.rentcar.User;

import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.avis.Avis;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.paiment.Paiment;
import com.stage.rentcar.permis.Permis;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.role.Role;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="\"users\"")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String cin;
    private String tel;
    private String image;
    private String adresse;
    private Genre genre ;
    private Boolean archive;
    private LocalDate dateNaiss;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean accountLocked;
    private boolean enabled;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy = "user")
    private List<Agence> agences;

    @OneToMany(mappedBy = "user")
    private List<Avis> avis;

    @OneToMany(mappedBy = "user")
    private List<Vehicule> vehicule;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permis_id")
    private Permis permis;

    @Override
    public String getName() {
        return email;
    }

    public UserRequest getDto(){
        UserRequest chef = new UserRequest();
        chef.setId(id);
        chef.setNom(nom);
        chef.setPrenom(prenom);
        chef.setDateNaiss(dateNaiss);
        chef.setEmail(email);
        chef.setPassword(password);
        chef.setEnabled(enabled);
        chef.setAccountLocked(accountLocked);
        chef.setCin(cin);
        chef.setGenre(genre);
        chef.setAdresse(adresse);
        chef.setTel(tel);
        if(image != null){
            byte[] imageBytes = FileUtils.readFileFromLocation(image);
            chef.setImage(imageBytes);
        }
        chef.setArchive(archive);
        chef.setRole("chef_agence");
      /*  if (!roles.isEmpty()) {
            chef.setRole(roles.iterator().next().getName().name());
        }*/
        return chef ;
    }
    public UserRequest getUserDto(){
        UserRequest u = new UserRequest();
        u.setId(id);
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setDateNaiss(dateNaiss);
        u.setEmail(email);
        u.setPassword(password);
        u.setEnabled(enabled);
        u.setAccountLocked(accountLocked);
        u.setCin(cin);
        u.setGenre(genre);
        u.setAdresse(adresse);
        u.setTel(tel);
        if(image != null){
            byte[] imageBytes = FileUtils.readFileFromLocation(image);
            u.setImage(imageBytes);
        }
        u.setArchive(archive);

        if (!roles.isEmpty()) {
            u.setRole(roles.iterator().next().getName().name());
        }
        return u ;
    }
    public UserRequest getUpdateDto(){
        UserRequest chef = new UserRequest();
        chef.setId(id);
        chef.setNom(nom);
        chef.setPrenom(prenom);
        chef.setDateNaiss(dateNaiss);
        chef.setEmail(email);
        chef.setEnabled(enabled);
        chef.setAccountLocked(accountLocked);
        chef.setCin(cin);
        chef.setTel(tel);
        chef.setGenre(genre);
        chef.setAdresse(adresse);
        chef.setRole("chef_agence");
        chef.setArchive(false);
        if(image != null){
            byte[] imageBytes = FileUtils.readFileFromLocation(image);
            chef.setImage(imageBytes);
        }
        return chef ;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName() {
        return nom + " " + prenom;
    }
}
