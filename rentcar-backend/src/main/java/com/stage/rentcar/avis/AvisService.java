package com.stage.rentcar.avis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AvisService {
    AvisRequest createAvis(AvisRequest avisRequest);
    List<AvisRequest> getAvisByVehicule(Integer vehiculeId);
    List<AvisRequest> getAvisByClient(Integer userId);
    List<AvisRequest> getAvisByType(AvisType avisType);
    AvisRequest updateAvis(Long avisId, AvisRequest avisRequest);
    void toggleArchive(Long id);
    long countTotalAvisByType(AvisType type);
    long countTotalAvis();
    long countTotalAvisByChefId(Integer chefId) ;
    List<AvisRequest> getAvisVehiculeByChefId(Integer chefId) ;
}
