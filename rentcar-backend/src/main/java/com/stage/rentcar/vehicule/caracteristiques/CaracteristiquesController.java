package com.stage.rentcar.vehicule.caracteristiques;

import com.stage.rentcar.agence.AgenceRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("caracteristiques")
@AllArgsConstructor
public class CaracteristiquesController {
    @Autowired
    private CaracteristiquesService caracteristiquesService ;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<CaracteristiquesRequest> createCaracteristiques(
            @Valid CaracteristiquesRequest request

    )  {
        CaracteristiquesRequest carac = caracteristiquesService.createCaracteristique(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carac);
    }


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<CaracteristiquesRequest>> getAllCaracteristiques() {
        List<CaracteristiquesRequest> c = caracteristiquesService.getAllCaracteristiques();
        return ResponseEntity.ok(c);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<CaracteristiquesRequest> updateCaracteristiques(
            @PathVariable Integer id,
            @Valid CaracteristiquesRequest request

    )  {
        CaracteristiquesRequest carac = caracteristiquesService.updateCaracteristiques(id ,request);
        return ResponseEntity.ok(carac);
    }
    @GetMapping("/vehicule/{vehiculeId}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<CaracteristiquesRequest> getCaracteristiquesByVehiculeId(@PathVariable Integer vehiculeId){
        CaracteristiquesRequest caracteristiquesRequest =caracteristiquesService.getCacteristiquesByVahiculeId(vehiculeId);
        return ResponseEntity.ok(caracteristiquesRequest);
    }
}
