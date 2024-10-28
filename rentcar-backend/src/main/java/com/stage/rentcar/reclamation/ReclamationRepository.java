package com.stage.rentcar.reclamation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {
    List<Reclamation> findAllByUserId(Integer userId);
}
