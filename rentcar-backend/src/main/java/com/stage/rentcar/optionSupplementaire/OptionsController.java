package com.stage.rentcar.optionSupplementaire;
import com.stage.rentcar.handler.ResourceNotFoundException;
import com.stage.rentcar.vehicule.VehiculeRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;


@RestController
@RequestMapping("options")
@RequiredArgsConstructor
public class OptionsController {
    @Autowired
    private OptionsService optionsService;

    @PostMapping
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<OptionsRequestChef> createOption(@RequestBody OptionsRequestChef optionsRequest) {
        OptionsRequestChef options = optionsService.createOption(optionsRequest);
        return ResponseEntity.ok(options);
    }


    @GetMapping
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<OptionsRequestChef>> getAllOptions() {
        List<OptionsRequestChef> optionsList = optionsService.getAllOptions();
        return ResponseEntity.ok(optionsList);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<OptionsRequestChef> updateOptions(@PathVariable Integer id, @RequestBody OptionsRequestChef optionsRequestChef) {
        OptionsRequestChef op = optionsService.updateOption(id, optionsRequestChef);
        return ResponseEntity.ok(op);
    }

    @GetMapping("/agence/{agenceId}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<OptionsRequestChef> getOptionsByAgenceId(@PathVariable Integer agenceId) {
        try {
            OptionsRequestChef optionsRequestChef = optionsService.getOptionsByAgenceId(agenceId);
            return ResponseEntity.ok(optionsRequestChef);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/prices/{agenceId}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public OptionPricesRequest getOptionsPricesByAgenceId(@PathVariable Integer agenceId) {
        return optionsService.getOptionsPricesByAgenceId(agenceId);
    }

}