package com.stage.rentcar.reclamation;

import com.stage.rentcar.avis.AvisRequest;
import com.stage.rentcar.avis.AvisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reclamation")
@RequiredArgsConstructor
public class ReclamationController {
    @Autowired
    private ReclamationService reclamationService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ReclamationRequest> createReclamation(
            @RequestBody ReclamationRequest request
    )  {
        ReclamationRequest reclamation = reclamationService.createReclamation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reclamation);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ReclamationRequest> updateReclamation(
            @PathVariable Long id,
            @RequestBody ReclamationRequest request
    )  {
        ReclamationRequest r = reclamationService.updateReclamation(id  , request);
        return ResponseEntity.ok(r);

    }
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<ReclamationRequest>> getReclamations() {
        List<ReclamationRequest> reclamationRequests = reclamationService.getAllReclamations();
        return ResponseEntity.ok(reclamationRequests);
    }

    @PutMapping("/archive/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<Void> archiveReclamations(
            @PathVariable Long id
    )  {
        reclamationService.toggleArchive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<ReclamationRequest>> getAvisByClient(@PathVariable Integer userId) {
        List<ReclamationRequest> reclamationRequests = reclamationService.getReclamationsByUser(userId);
        return ResponseEntity.ok(reclamationRequests);
    }



    @GetMapping("/count")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public long getTotalReclamations() {
        return reclamationService.countTotalRec();
    }




}
