package com.stage.rentcar.optionSupplementaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Integer> {
    Options findByAgenceId(Integer agenceId);

}
