package com.stage.rentcar.vehicule;

import com.stage.rentcar.vehicule.categorie.ECategorie;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VehiculeService {
    VehiculeRequest createVehicule(VehiculeRequest request) throws RoleNotFoundException;
    List<VehiculeRequest> getAllVehicule();
    VehiculeRequest getVehiculeById(Integer id) ;
    VehiculeRequest updateVehicule(Integer id, VehiculeRequest vehiculeRequest) throws RoleNotFoundException  ;
    void toggleArchive(Integer id);
    void uploadImage(MultipartFile file, Authentication connectedUser, Integer id);

    List<VehiculeRequest> getVehiculeByChefAgence(Integer id);
    List<VehiculeRequest> getVehiculesByCategorieAndChef(String categorieName , Integer chefId) ;
    List<VehiculeRequest> searchVehiculeByMarque(String marqueNom);
    List<VehiculeRequest> searchVehiculeByNom(String nom);
    List<VehiculeRequest> findVehiculesByPrixRange(Double minPrix, Double maxPrix) ;
    List<VehiculeRequest> searchVehiculeByChef(String nom);
    List<VehiculeRequest> getAllVehiculesByCategorie(String categorieName) throws RoleNotFoundException;
    List<VehiculeRequest> searchVehicules(String nom, String marqueNom, ECategorie categorie, Double minPrix, Double maxPrix);
    List<VehiculeRequest> searchAvailableVehicules(
            String agenceNom, Double kmSortie, String categorie,
            LocalDateTime dateDepart, LocalDateTime dateRetour);

    List<VehiculeRequest> searchAvailableVehiculesByDateRangeAndCategory (
            String categorie, LocalDateTime dateDepart, LocalDateTime dateRetour);
    List<VehiculeRequest> searchAvailableVehiculesByDateRange (
             LocalDateTime dateDepart, LocalDateTime dateRetour);

    List<VehiculeRequest> searchAvailableVehiculesByDateRangeAndMarque (
            LocalDateTime dateDepart, LocalDateTime dateRetour , Integer marqueId);

    Map<String, Double> getVehiculePrixRange();

    List<VehicleCategoryStatistics> getVehicleCategorieStatistics();
    List<VehicleCategoryStatistics> getVehicleCategorieStatisticsByChefId(Long chefId);
}
