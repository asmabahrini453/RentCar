package com.stage.rentcar.vehicule.marque;

import com.stage.rentcar.User.User;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("marque")
@AllArgsConstructor
public class MarqueController {

    private final MarqueService marqueService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<MarqueRequest> createMarque(
            @Valid MarqueRequest request
    ) {
        MarqueRequest marque = marqueService.createMarque(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(marque);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<MarqueRequest>> getAllMarque() {
        List<MarqueRequest> marqueRequests = marqueService.getAllMarque();
        return ResponseEntity.ok(marqueRequests);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<MarqueRequest> updateMarque(
            @PathVariable Integer id,
            @Valid @RequestBody MarqueRequest request
    ) {
        MarqueRequest marqueRequest = marqueService.updateMarque(id, request);
        return ResponseEntity.ok(marqueRequest);
    }


}
