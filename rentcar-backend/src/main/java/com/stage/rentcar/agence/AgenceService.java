package com.stage.rentcar.agence;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AgenceService {

    AgenceRequest createAgence(AgenceRequest agenceRequest);
    AgenceRequest updateAgence(Integer id, AgenceRequest agenceRequest);
    List<AgenceRequest> getAllAgence();
    AgenceRequest getAgenceById (Integer id);
    List<AgenceRequest> getAgenceByChefAgence(Integer userId);

    void uploadAgenceLogo(MultipartFile file, Authentication connectedUser, Integer id);
    void toggleArchive(Integer id);
    List<AgenceRequest> SearchAgenceByNom(String nom);
    List<AgenceRequest> searchAgenceByLocalisation(String localisation);
    long countTotalAgences();
    long countTotalAgencesByChefId(Integer chefId);

}
