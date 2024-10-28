package com.stage.rentcar.agence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgenceRepository extends JpaRepository<Agence,Integer> {
    List<Agence> findAllByUserId(Integer userId);
    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Agence> findAllAgenceByNom(@Param("nom") String nom);
    @Query("SELECT a FROM Agence a WHERE LOWER(a.localisation) LIKE LOWER(CONCAT('%', :localisation, '%'))")
    List<Agence> findAllAgenceByLocalisation(@Param("localisation") String localisation);

    long countByUserId(Integer userId);
}
