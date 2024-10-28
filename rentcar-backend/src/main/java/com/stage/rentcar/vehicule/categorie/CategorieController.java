package com.stage.rentcar.vehicule.categorie;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategorieController {

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ECategorie[] getAllCategories() {
        return ECategorie.values();
    }
}
