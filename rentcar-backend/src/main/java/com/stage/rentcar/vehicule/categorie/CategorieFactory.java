package com.stage.rentcar.vehicule.categorie;

import com.stage.rentcar.role.ERole;
import com.stage.rentcar.role.Role;
import com.stage.rentcar.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;

@Component

public class CategorieFactory {
    @Autowired
    CategorieRepository categorieRepository;

    public Categorie getInstance(String categorie) throws RoleNotFoundException {
        switch (categorie) {
            case "VOITURE" -> {
                return categorieRepository.findByName(ECategorie.VOITURE);
            }
            case "CAMION" -> {
                return categorieRepository.findByName(ECategorie.CAMION);
            }
            case "BUS" -> {
                return categorieRepository.findByName(ECategorie.BUS);
            }
            default -> throw  new RoleNotFoundException("pas de categorie: " +  categorie);
        }
    }
}
