package com.stage.rentcar.vehicule;

import com.stage.rentcar.agence.AgenceRequest;
import com.stage.rentcar.vehicule.categorie.ECategorie;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("vehicule")
@AllArgsConstructor
public class VehiculeController {
    @Autowired
    private VehiculeService  vehiculeService ;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<VehiculeRequest> createVehicule(@RequestBody VehiculeRequest vehiculeRequest)
            throws RoleNotFoundException {
            VehiculeRequest createdVehicule = vehiculeService.createVehicule(vehiculeRequest);
            return new ResponseEntity<>(createdVehicule, HttpStatus.CREATED);

    }


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<VehiculeRequest>> getAllVehicule(){
        List<VehiculeRequest> vehiculeList = vehiculeService.getAllVehicule();
        return ResponseEntity.ok().body(vehiculeList);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<VehiculeRequest> getVehiculeById(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(vehiculeService.getVehiculeById(id));
    }
    @GetMapping("/chef/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<VehiculeRequest>> getVehiculeByChefAgence(@PathVariable Integer id ){
        List<VehiculeRequest> vehiculeRequests=vehiculeService.getVehiculeByChefAgence(id);
        return ResponseEntity.ok(vehiculeRequests);
    }

    @GetMapping("/categorie-chef")
    public ResponseEntity<List<VehiculeRequest>> getVehiculesByCategorieAndChef(
            @RequestParam String categorieName,
            @RequestParam Integer chefId) {

        List<VehiculeRequest> vehicules = vehiculeService.getVehiculesByCategorieAndChef(categorieName, chefId);

        if (vehicules.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(vehicules);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<VehiculeRequest> updateVehicule(@PathVariable Integer id, @RequestBody VehiculeRequest vehiculeRequest) throws RoleNotFoundException {
        VehiculeRequest vehicule = vehiculeService.updateVehicule(id, vehiculeRequest);
        return ResponseEntity.ok(vehicule);
    }

    @PostMapping(value ="/image/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<?> uploadImage(
            @PathVariable Integer id ,
            @Parameter()
            @RequestPart("file") MultipartFile file ,
            Authentication connectedUser
    ){
        vehiculeService.uploadImage(file, connectedUser , id);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/archive/{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> archiveVehicule(
            @PathVariable Integer id
    )  {
        vehiculeService.toggleArchive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search-nom/{nom}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')  or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> SearchVehiculeByNom(@PathVariable String nom ){
        List<VehiculeRequest> vehiculeRequests=vehiculeService.searchVehiculeByNom(nom);
        return ResponseEntity.ok(vehiculeRequests);
    }

    @GetMapping("/search-marque/{nom}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> searchVehiculeByMarque(@PathVariable("nom") String nom) {
        List<VehiculeRequest> vehiculeRequests = vehiculeService.searchVehiculeByMarque(nom);
        return ResponseEntity.ok(vehiculeRequests);
    }


    @GetMapping("/search-categorie/{categorieName}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> getAllVehiculesByCategorie(@PathVariable String categorieName) {
        try {
            List<VehiculeRequest> vehicules = vehiculeService.getAllVehiculesByCategorie(categorieName);
            return ResponseEntity.ok(vehicules);
        } catch (RoleNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search-prix")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>>  searchByPrixRange(@RequestParam Double minPrix, @RequestParam Double maxPrix) {
        List<VehiculeRequest> vehicules = vehiculeService.findVehiculesByPrixRange(minPrix, maxPrix);
        return ResponseEntity.ok(vehicules);
    }
    @GetMapping("/search-multiple")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> searchVehicules(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String marqueNom,
            @RequestParam(required = false) ECategorie categorie,
            @RequestParam(required = false) Double minPrix,
            @RequestParam(required = false) Double maxPrix) {

        List<VehiculeRequest> vehicules = vehiculeService.searchVehicules(nom, marqueNom, categorie, minPrix, maxPrix);
        return ResponseEntity.ok(vehicules);
    }




    @GetMapping("/search-chef/{nom}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<VehiculeRequest>> searchVehiculeByChef(@PathVariable String nom) {
        List<VehiculeRequest> vehiculeRequests = vehiculeService.searchVehiculeByChef(nom);
        return ResponseEntity.ok(vehiculeRequests);
    }


    @GetMapping("/rechercher")
    @PreAuthorize(" hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> rechercherVehicules(
            @RequestParam(value = "agenceNom") String agenceNom,
            @RequestParam(value = "kmSortie") Double kmSortie,
            @RequestParam(value = "categorie") String categorie,
            @RequestParam(value = "dateDepart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepart,
            @RequestParam(value = "dateRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRetour) {
        List<VehiculeRequest> vehicules = vehiculeService.searchAvailableVehicules(
                agenceNom, kmSortie, categorie, dateDepart, dateRetour);

        return ResponseEntity.ok(vehicules);
    }

    @GetMapping("/rechercher-date-categorie")
    @PreAuthorize(" hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> chercherVehiculesParDateEtCategorie(
            @RequestParam(value = "categorie") String categorie,
            @RequestParam(value = "dateDepart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepart,
            @RequestParam(value = "dateRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRetour) {
        List<VehiculeRequest> vehicules = vehiculeService.searchAvailableVehiculesByDateRangeAndCategory(
               categorie, dateDepart, dateRetour);

        return ResponseEntity.ok(vehicules);
    }


    @GetMapping("/rechercher-date-marque")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> chercherVehiculesParDateEtMarque(
            @RequestParam("dateDepart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepart,
            @RequestParam("dateRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRetour,
            @RequestParam("marqueId") Integer marqueId) {
        List<VehiculeRequest> vehicules = vehiculeService.searchAvailableVehiculesByDateRangeAndMarque(dateDepart, dateRetour, marqueId);
        return ResponseEntity.ok(vehicules);
    }

    @GetMapping("/rechercher-date")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<VehiculeRequest>> chercherVehiculesParDate(
            @RequestParam("dateDepart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepart,
            @RequestParam("dateRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRetour
          ) {
        List<VehiculeRequest> vehicules = vehiculeService.searchAvailableVehiculesByDateRange(dateDepart, dateRetour);
        return ResponseEntity.ok(vehicules);
    }


    @GetMapping("/prix-range")
    public ResponseEntity<Map<String, Double>> getVehiculePriceRange() {
        Map<String, Double> priceRange = vehiculeService.getVehiculePrixRange();
        return ResponseEntity.ok(priceRange);
    }


    @GetMapping("/stat")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public List<VehicleCategoryStatistics> getVehicleCategoryStatistics() {
        return vehiculeService.getVehicleCategorieStatistics();
    }

    @GetMapping("/stat-chef/{chefId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<VehicleCategoryStatistics>> getVehicleCategoryStatisticsByChefId(
            @PathVariable Long chefId) {
        try {
            List<VehicleCategoryStatistics> statistics = vehiculeService.getVehicleCategorieStatisticsByChefId(chefId);
            return ResponseEntity.ok(statistics);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
