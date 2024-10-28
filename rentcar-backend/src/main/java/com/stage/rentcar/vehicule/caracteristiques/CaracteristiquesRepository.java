package com.stage.rentcar.vehicule.caracteristiques;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaracteristiquesRepository extends JpaRepository<Caracteristiques, Integer> {

    @Query("SELECT c FROM Caracteristiques c JOIN c.vehicules v WHERE v.id = :vehiculeId")
    Caracteristiques findByVehiculeId(Integer vehiculeId);

}
