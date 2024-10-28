package com.stage.rentcar.agence;

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

import java.net.Inet4Address;
import java.util.List;

@RestController
@RequestMapping("agence")
@AllArgsConstructor

public class AgenceController {
    @Autowired
    private AgenceService agenceService;


    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AgenceRequest> createAgence(
            @Valid AgenceRequest agenceRequest

    )  {
        AgenceRequest agence = agenceService.createAgence(agenceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agence);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AgenceRequest> updateAgence(
            @PathVariable Integer id,
            @Valid AgenceRequest agenceRequest
    )  {
        AgenceRequest agence = agenceService.updateAgence(id  , agenceRequest);
        return ResponseEntity.ok(agence);

    }
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<AgenceRequest>> getAllAgence() {
        List<AgenceRequest> agenceList = agenceService.getAllAgence();
        return ResponseEntity.ok(agenceList);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<AgenceRequest> getAgenceById(@PathVariable Integer id) {
        return ResponseEntity.ok(agenceService.getAgenceById(id));
    }
    @GetMapping("/chef")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<AgenceRequest>> getAgenceByChefAgence(@RequestParam Integer userId) {
        List<AgenceRequest> agenceList = agenceService.getAgenceByChefAgence(userId);
        return ResponseEntity.ok(agenceList);
    }

    @PostMapping(value ="/logo/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<?> uploadAgenceLogo (
            @PathVariable Integer id ,
            @Parameter()
            @RequestPart("file") MultipartFile file ,
            Authentication connectedUser
    ){
        agenceService.uploadAgenceLogo(file, connectedUser , id);
        return ResponseEntity.accepted().build();
    }
    @PutMapping("/archive/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Void> archiveAgence(
            @PathVariable Integer id
    )  {
         agenceService.toggleArchive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/{nom}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<AgenceRequest>> SearchAgenceByNom(@PathVariable String nom ){
        List<AgenceRequest> agenceRequestList=agenceService.SearchAgenceByNom(nom);
        return ResponseEntity.ok(agenceRequestList);
    }
     @GetMapping("/search-loc")
    @PreAuthorize("hasRole('ROLE_CLIENT') ")
    public ResponseEntity<List<AgenceRequest>> SearchAgenceByLocalisation(@RequestParam String localisation ){
        List<AgenceRequest> agenceRequestList=agenceService.searchAgenceByLocalisation(localisation);
        return ResponseEntity.ok(agenceRequestList);
    }


    @GetMapping("/count")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public long getTotalAgences() {
        return agenceService.countTotalAgences();
    }


    @GetMapping("/count-chef/{chefId}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Long> getTotalAgencesByChefId(@PathVariable Integer chefId) {
        long totalAgences = agenceService.countTotalAgencesByChefId(chefId);
        return ResponseEntity.ok(totalAgences);
    }

}
