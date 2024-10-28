package com.stage.rentcar.avis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis,Long> {
    List<Avis> findByVehiculeId(Integer vehiculeId);
    List<Avis> findByUserId(Integer userId);
    List<Avis> findByType(AvisType type);
    long countByType(AvisType type);
    long countByVehiculeIdIn(List<Integer> vehiculeIds);
}
