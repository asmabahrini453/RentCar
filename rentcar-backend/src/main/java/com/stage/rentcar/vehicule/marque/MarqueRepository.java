package com.stage.rentcar.vehicule.marque;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MarqueRepository extends JpaRepository<Marque, Integer> {
    Marque findByNom(String nom);

}
