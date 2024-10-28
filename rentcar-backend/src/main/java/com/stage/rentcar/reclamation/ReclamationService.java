package com.stage.rentcar.reclamation;

import java.util.List;

public interface ReclamationService   {
    List<ReclamationRequest> getAllReclamations();
    ReclamationRequest updateReclamation(Long id, ReclamationRequest request) ;
    ReclamationRequest createReclamation(ReclamationRequest request);
    List<ReclamationRequest> getAllReclamationsByUser(Integer userId);
    void toggleArchive(Long id);
    List<ReclamationRequest> getReclamationsByUser(Integer userId);
    long countTotalRec();
}
