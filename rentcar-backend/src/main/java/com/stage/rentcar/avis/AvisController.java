package com.stage.rentcar.avis;

import com.stage.rentcar.agence.AgenceRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("avis")
@RequiredArgsConstructor
public class AvisController {
    @Autowired
    private AvisService avisService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<AvisRequest> createAvis(
            @RequestBody AvisRequest avisRequest
    )  {
        AvisRequest avis = avisService.createAvis(avisRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avis);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<AvisRequest> updateAvis(
            @PathVariable Long id,
            @RequestBody AvisRequest avisRequest
    )  {
        AvisRequest avis = avisService.updateAvis(id  , avisRequest);
        return ResponseEntity.ok(avis);

    }
    @GetMapping("/vehicule/{vehiculeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<AvisRequest>> getAvisByVehicule(@PathVariable Integer vehiculeId) {
        List<AvisRequest> avisRequests = avisService.getAvisByVehicule(vehiculeId);
        return ResponseEntity.ok(avisRequests);
    }
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<AvisRequest>> getAvisByClient(@PathVariable Integer clientId) {
        List<AvisRequest> avisRequests = avisService.getAvisByClient(clientId);
        return ResponseEntity.ok(avisRequests);
    }
    @GetMapping("/chef/{chefId}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<AvisRequest>> getAvisByChefId(@PathVariable Integer chefId) {
        List<AvisRequest> avisRequests = avisService.getAvisVehiculeByChefId(chefId);

        return ResponseEntity.ok(avisRequests);
    }
    @GetMapping("/platforme")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<AvisRequest>> getAvisPlatform() {
        List<AvisRequest> avisRequests = avisService.getAvisByType(AvisType.PLATFORME);
        return ResponseEntity.ok(avisRequests);
    }
    @GetMapping("/vehicule")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')  ")
    public ResponseEntity<List<AvisRequest>> getAvisVehicule() {
        List<AvisRequest> avisRequests = avisService.getAvisByType(AvisType.VEHICULE);
        return ResponseEntity.ok(avisRequests);
    }

    @PutMapping("/archive/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') ")
    public ResponseEntity<Void> archiveAvis(
            @PathVariable Long id
    )  {
        avisService.toggleArchive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public long getTotalAvis() {
        return avisService.countTotalAvis();
    }

    @GetMapping("/count/{type}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public long getTotalAvisByType(@PathVariable AvisType type) {
        return avisService.countTotalAvisByType(type);
    }

    @GetMapping("/count-chef/{chefId}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Long> getTotalAvisByChefId(@PathVariable Integer chefId) {
        long totalAvis = avisService.countTotalAvisByChefId(chefId);
        return ResponseEntity.ok(totalAvis);
    }

}
