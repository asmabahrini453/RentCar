package com.stage.rentcar.vehicule.categorie;

import com.stage.rentcar.role.ERole;
import com.stage.rentcar.role.Role;
import com.stage.rentcar.role.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategorieDataSeeder {
    @Autowired
    private CategorieRepository categorieRepository;

    @EventListener
    @Transactional
    public void LoadCategories(ContextRefreshedEvent event) {

        List<ECategorie> categories = Arrays.stream(ECategorie.values()).toList();

        for(ECategorie eCategorie: categories) {
            if (categorieRepository.findByName(eCategorie)==null) {
                categorieRepository.save(new Categorie(eCategorie));
            }
        }

    }
}
