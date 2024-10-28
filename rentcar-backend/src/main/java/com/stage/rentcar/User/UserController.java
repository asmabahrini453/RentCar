package com.stage.rentcar.User;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("user")
@AllArgsConstructor

public class UserController {

    @Autowired
    private UserService userService ;

    @GetMapping("/chef")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRequest>> getAllChefAgence (){
        List<UserRequest> chefs = userService.getAllChefAgence();
        return  ResponseEntity.ok(chefs);
    }
    @GetMapping("/client")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRequest>> getAllClient (){
        List<UserRequest> chefs = userService.getAllClient();
        return  ResponseEntity.ok(chefs);
    }

    @GetMapping("/chef/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<UserRequest> getChefAgenceById (@PathVariable Integer id){
        UserRequest chef = userService.getChefAgenceById(id);
        return  ResponseEntity.ok(chef);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<UserRequest> getUserById (@PathVariable Integer id){
        UserRequest user = userService.getUserById(id);
        return  ResponseEntity.ok(user);
    }


    @GetMapping("/client/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')  or hasRole('ROLE_CLIENT')")
    public ResponseEntity<UserRequest> getClientById (@PathVariable Integer id){
        UserRequest chef = userService.getClientById(id);
        return  ResponseEntity.ok(chef);
    }


    @PostMapping("/chef")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserRequest> createChefAgence(@Valid UserRequest userRequest) throws RoleNotFoundException {
        UserRequest chef= userService.createChefAgence(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chef);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<UserRequest> updateUser (@PathVariable Integer id , @RequestBody UserRequest userRequest){
        UserRequest chef = userService.updateUser(id, userRequest);
        return  ResponseEntity.ok(chef);
    }
    @PutMapping(value = "/password/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Integer id,
            @Valid RequestChangePassword requestChangePassword
    ) {
        userService.changePassword(id, requestChangePassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/archive-chef/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> archiveChefAgence (@PathVariable Integer id){
        userService.toggleArchive(id);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/archive-client/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Void> archiveClient (@PathVariable Integer id){
        userService.toggleArchive(id);
        return  ResponseEntity.ok().build();
    }
    @GetMapping("/search-chef")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRequest>> searchChefAgence(
            @RequestParam String nom) {
        List<UserRequest> userRequests = userService.searchChefByNom(nom);
        return ResponseEntity.ok(userRequests);
    }
    @GetMapping("/search-client")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<UserRequest>> searchClient(
            @RequestParam String nom) {
        List<UserRequest> userRequests = userService.searchClientByNom(nom);
        return ResponseEntity.ok(userRequests);
    }

    @PostMapping(value ="/image/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> uploadImage(
            @PathVariable Integer id ,
            @Parameter()
            @RequestPart("file") MultipartFile file ,
            Authentication connectedUser
    ){
        userService.uploadImage(file, connectedUser , id);
        return ResponseEntity.accepted().build();
    }

//STATISTIQUES
    @GetMapping("/stat_nbr_users")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public UserStatisticsRequest getUserStatistics() {

        return userService.getUserStatics();
    }

    @GetMapping("/stat_client_mois")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public List<MonthlyUserStatisticsRequest> getMonthlyClientStatistics() {
        return userService.getMonthlyClientStatistics();
    }

}
