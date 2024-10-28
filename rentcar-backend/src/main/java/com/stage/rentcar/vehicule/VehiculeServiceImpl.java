package com.stage.rentcar.vehicule;

import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRepository;
import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.agence.AgenceRepository;
import com.stage.rentcar.agence.AgenceRequest;
import com.stage.rentcar.file.FileStorageService;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.vehicule.caracteristiques.Caracteristiques;
import com.stage.rentcar.vehicule.caracteristiques.CaracteristiquesRepository;
import com.stage.rentcar.vehicule.caracteristiques.CaracteristiquesRequest;
import com.stage.rentcar.vehicule.caracteristiques.CaracteristiquesService;
import com.stage.rentcar.vehicule.categorie.Categorie;
import com.stage.rentcar.vehicule.categorie.CategorieFactory;
import com.stage.rentcar.vehicule.categorie.CategorieRepository;
import com.stage.rentcar.vehicule.categorie.ECategorie;
import com.stage.rentcar.vehicule.marque.Marque;
import com.stage.rentcar.vehicule.marque.MarqueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService{
    private static final Logger logger = LoggerFactory.getLogger(VehiculeServiceImpl.class);
    @Autowired
    private VehiculeRepository vehiculeRepository ;
    @Autowired
    private CategorieRepository categorieRepository  ;
    @Autowired
    private AgenceRepository agenceRepository  ;
    @Autowired
    private MarqueRepository marqueRepository  ;
    @Autowired
    private CaracteristiquesRepository caracteristiquesRepository  ;
    @Autowired
    private UserRepository userRepository  ;
    @Autowired
    private FileStorageService fileStorageService  ;
    @Autowired
    private CategorieFactory categorieFactory;
    @Autowired
    private CaracteristiquesService caracteristiquesService;

    @Override
    public List<VehiculeRequest> getAllVehicule(){
        List<Vehicule> vehiculeRequestList = vehiculeRepository.findAll();
        return vehiculeRequestList.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }

    @Override
    public VehiculeRequest getVehiculeById(Integer id){
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow();
        return vehicule.getDto();
    }
    @Override
    public List<VehiculeRequest> getVehiculeByChefAgence(Integer id){
        User user = userRepository.findByIdAndRoles_Name(id , ERole.ROLE_CHEF_AGENCE)
                .orElseThrow(()->new EntityNotFoundException("chef d'agence inexistant"));
        List<Vehicule> vehicules = vehiculeRepository.findAllByUserId(id);

        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehiculeRequest createVehicule(VehiculeRequest vehiculeRequest) throws RoleNotFoundException {
        Marque marque;
        if (vehiculeRequest.getMarqueId() != null) {
            marque = marqueRepository.findById(vehiculeRequest.getMarqueId())
                    .orElseThrow(() -> new EntityNotFoundException("Marque not found with ID: " + vehiculeRequest.getMarqueId()));
        } else {
            marque = new Marque();
            marque.setNom(vehiculeRequest.getMarqueNom());
            marque = marqueRepository.save(marque);
        }

        Categorie categorie = categorieFactory.getInstance(vehiculeRequest.getCategorie());

        Caracteristiques caracteristiques = vehiculeRequest.getCaracteristiques().toEntity();
        caracteristiques = caracteristiquesRepository.save(caracteristiques);

        Agence agence = agenceRepository.findById(vehiculeRequest.getAgenceId())
                .orElseThrow(() -> new EntityNotFoundException("Agence not found with ID: " + vehiculeRequest.getAgenceId()));

        Optional<User> userOptional = userRepository.findById(vehiculeRequest.getUserId());
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("User inexistant avec ID: " + vehiculeRequest.getUserId());
        }

        User user = userOptional.get();
        boolean isChefAgence = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_CHEF_AGENCE));

        if (!isChefAgence) {
            throw new IllegalArgumentException("User avec ID " + user.getId() + " n'est pas un 'chef agence'.");
        }

        Vehicule vehicule = Vehicule.builder()
                .nom(vehiculeRequest.getNom())
                .description(vehiculeRequest.getDescription())
                .anneeCirculation(vehiculeRequest.getAnneeCirculation())
                .couleur(vehiculeRequest.getCouleur())
                .immatriculation(vehiculeRequest.getImmatriculation())
                .typeAssurance(vehiculeRequest.getTypeAssurance())
                .prixParJour(vehiculeRequest.getPrixParJour())
                .disponibilite(vehiculeRequest.getDisponibilite())
                .kmSortie(vehiculeRequest.getKmSortie())
                .fraisAnnulation(vehiculeRequest.getFraisAnnulation())
                .archive(false)
                .agence(agence)
                .marque(marque)
                .categorie(categorie)
                .user(user)
                .caracteristiques(caracteristiques)
                .build();

        vehicule = vehiculeRepository.save(vehicule);


        return vehicule.getDto();
    }



    @Override
    @Transactional
    public VehiculeRequest updateVehicule(Integer id, VehiculeRequest vehiculeRequest) throws RoleNotFoundException {
        Optional<Vehicule> vehiculeOptional = vehiculeRepository.findById(id);
        if (vehiculeOptional.isPresent()) {
            Vehicule updatedVehicule = vehiculeOptional.get();
            Marque marque;

            if (vehiculeRequest.getMarqueId() != null) {
                marque = marqueRepository.findById(vehiculeRequest.getMarqueId())
                        .orElseThrow(() -> new EntityNotFoundException("Marque not found with ID: " + vehiculeRequest.getMarqueId()));
            } else {
                if (vehiculeRequest.getMarqueNom() == null || vehiculeRequest.getMarqueNom().isEmpty()) {
                    throw new IllegalArgumentException("Marque name cannot be null or empty when creating a new Marque");
                }
                marque = new Marque();
                marque.setNom(vehiculeRequest.getMarqueNom());
                marque = marqueRepository.save(marque);
            }

            if (vehiculeRequest.getAgenceId() != null) {
                Agence agence = agenceRepository.findById(vehiculeRequest.getAgenceId())
                        .orElseThrow(() -> new EntityNotFoundException("Agence not found with ID: " + vehiculeRequest.getAgenceId()));
                updatedVehicule.setAgence(agence);
            }

            if (vehiculeRequest.getCategorie() != null) {
                Categorie categorie = categorieFactory.getInstance(vehiculeRequest.getCategorie());
                updatedVehicule.setCategorie(categorie);
            }

            if (vehiculeRequest.getCaracteristiques() != null) {
                Caracteristiques caracteristiques = vehiculeRequest.getCaracteristiques().toEntity();
                if (caracteristiques.getId() != null) {
                    caracteristiques.setId(vehiculeRequest.getCaracteristiques().getId());
                }
                caracteristiques = caracteristiquesRepository.save(caracteristiques);
                updatedVehicule.setCaracteristiques(caracteristiques);
            }

            if (vehiculeRequest.getUserId() != null) {
                User user = userRepository.findById(vehiculeRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + vehiculeRequest.getUserId()));

                boolean isChefAgence = user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(ERole.ROLE_CHEF_AGENCE));

                if (!isChefAgence) {
                    throw new IllegalArgumentException("User with ID " + user.getId() + " is not a 'chef agence'.");
                }

                updatedVehicule.setUser(user);
            }

            if (vehiculeRequest.getNom() != null) {
                updatedVehicule.setNom(vehiculeRequest.getNom());
            }
            if (vehiculeRequest.getDescription() != null) {
                updatedVehicule.setDescription(vehiculeRequest.getDescription());
            }
            if (vehiculeRequest.getAnneeCirculation() != null) {
                updatedVehicule.setAnneeCirculation(vehiculeRequest.getAnneeCirculation());
            }
            if (vehiculeRequest.getCouleur() != null) {
                updatedVehicule.setCouleur(vehiculeRequest.getCouleur());
            }
            if (vehiculeRequest.getImmatriculation() != null) {
                updatedVehicule.setImmatriculation(vehiculeRequest.getImmatriculation());
            }
            if (vehiculeRequest.getTypeAssurance() != null) {
                updatedVehicule.setTypeAssurance(vehiculeRequest.getTypeAssurance());
            }
            if (vehiculeRequest.getDisponibilite() != null) {
                updatedVehicule.setDisponibilite(vehiculeRequest.getDisponibilite());
            }
            if (vehiculeRequest.getPrixParJour() != null) {
                updatedVehicule.setPrixParJour(vehiculeRequest.getPrixParJour());
            }
            if (vehiculeRequest.getKmSortie() != null) {
                updatedVehicule.setKmSortie(vehiculeRequest.getKmSortie());
            }
             if (vehiculeRequest.getFraisAnnulation() != null) {
                updatedVehicule.setFraisAnnulation(vehiculeRequest.getFraisAnnulation());
            }

            updatedVehicule.setMarque(marque);

            Vehicule savedVehicule = vehiculeRepository.save(updatedVehicule);
            return savedVehicule.getDto();

        } else {
            throw new EntityNotFoundException("Vehicule not found with ID: " + id);
        }
    }


    @Override
    public void uploadImage(MultipartFile file, Authentication connectedUser, Integer id){
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("vehicule intrrouvable"));
        User user = ((User) connectedUser.getPrincipal());
        var image = fileStorageService.saveFile(file,user.getId());
        vehicule.setImage(image);
        vehiculeRepository.save(vehicule);

    }

    @Override
    public void toggleArchive(Integer id){
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("vehicule introuvable pour l'id"+id));
        boolean currentStatus = vehicule.getArchive();
        vehicule.setArchive(!currentStatus);
        vehiculeRepository.save(vehicule);
    }

    @Override
    public List<VehiculeRequest> searchVehiculeByNom(String nom){
        List<Vehicule> vehicules = vehiculeRepository.findAllVehiculeByNom(nom) ;
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }

    @Override
    public List<VehiculeRequest> searchVehiculeByChef(String nom) {
        List<User> chefs = userRepository.findByNomAndRoles_Name(nom, ERole.ROLE_CHEF_AGENCE);
        if (chefs.isEmpty()) {
            throw new EntityNotFoundException("No chef d'agence found with name: " + nom);
        }

        List<Vehicule> vehicules = chefs.stream()
                .flatMap(chef -> vehiculeRepository.findAllByUserId(chef.getId()).stream())
                .collect(Collectors.toList());

        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }

    @Override
    public List<VehiculeRequest> searchVehiculeByMarque(String marqueNom) {
        List<Vehicule> vehicules = vehiculeRepository.findByMarqueNomContainingIgnoreCase(marqueNom);
        return vehicules.stream()
                .map(Vehicule::getDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<VehiculeRequest> getAllVehiculesByCategorie(String categorieName) throws RoleNotFoundException {
        Categorie categorie = categorieRepository.findByName(ECategorie.valueOf(categorieName.toUpperCase()));
        if (categorie == null) {
            throw new RoleNotFoundException("Categorie introuvables: " + categorieName);
        }
        List<Vehicule> vehicules =vehiculeRepository.findByCategorie(categorie);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }

    @Override
    public  List<VehiculeRequest> getVehiculesByCategorieAndChef(String categorieName , Integer chefId){
        Categorie categorie = categorieRepository.findByName(ECategorie.valueOf(categorieName.toUpperCase()));
        List<Vehicule> vehicule = vehiculeRepository.findByCategorieAndUserId(categorie, chefId);
        return vehicule.stream()
                .map(Vehicule::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculeRequest> searchVehicules(String nom, String marqueNom, ECategorie categorie, Double minPrix, Double maxPrix) {
        List<Vehicule> vehicules =vehiculeRepository.findVehicules(nom, marqueNom, categorie, minPrix, maxPrix);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());

    }
    @Override
    public List<VehiculeRequest> searchAvailableVehicules(
            String agenceNom, Double kmSortie, String categorie,
            LocalDateTime dateDepart, LocalDateTime dateRetour) {
        List<Vehicule> vehicules = vehiculeRepository.findAvailableVehicules(
                agenceNom, kmSortie, categorie, dateDepart, dateRetour);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }
    @Override
    public List<VehiculeRequest> searchAvailableVehiculesByDateRangeAndCategory(
            String categorie, LocalDateTime dateDepart, LocalDateTime dateRetour) {
        List<Vehicule> vehicules = vehiculeRepository.findAvailableVehiculesByDateRangeAndCategory(
                categorie, dateDepart, dateRetour);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }
    @Override
    public List<VehiculeRequest> searchAvailableVehiculesByDateRangeAndMarque(
            LocalDateTime dateDepart, LocalDateTime dateRetour, Integer marqueId) {
        List<Vehicule> vehicules = vehiculeRepository.findAvailableVehiculesByDateRangeAndMarque(marqueId, dateDepart, dateRetour);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }
    @Override
    public List<VehiculeRequest> searchAvailableVehiculesByDateRange(
            LocalDateTime dateDepart, LocalDateTime dateRetour) {
        List<Vehicule> vehicules = vehiculeRepository.findAvailableVehiculesByDateRange(dateDepart, dateRetour);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());
    }

    @Override
    public List<VehiculeRequest> findVehiculesByPrixRange(Double minPrix, Double maxPrix) {
        List<Vehicule> vehicules =  vehiculeRepository.findByPrixParJourBetween(minPrix, maxPrix);
        return vehicules.stream().map(Vehicule::getDto).collect(Collectors.toList());

    }


    @Override
    public Map<String, Double> getVehiculePrixRange() {
        Double minPrix = vehiculeRepository.findMinPrix();
        Double maxPrix = vehiculeRepository.findMaxPrix();

        Map<String, Double> priceRange = new HashMap<>();
        priceRange.put("minPrix", minPrix);
        priceRange.put("maxPrix", maxPrix);

        return priceRange;
    }



//STAT
@Override
public List<VehicleCategoryStatistics> getVehicleCategorieStatistics() {
    List<Object[]> results = categorieRepository.countVehiclesByCategorie();
    long totalVehicles = vehiculeRepository.count();

    return results.stream()
            .map(result -> new VehicleCategoryStatistics(
                    result[0].toString(),
                    ((Number) result[1]).longValue(),
                    ((Number) result[1]).longValue() * 100.0 / totalVehicles
            ))
            .collect(Collectors.toList());
}

    @Override
    public List<VehicleCategoryStatistics> getVehicleCategorieStatisticsByChefId(Long chefId) {
        // Fetch the total number of vehicles for calculation of percentage
        long totalVehicles = vehiculeRepository.count();

        // Fetch vehicle category counts specific to the chef ID
        List<Object[]> results = categorieRepository.countVehiclesByCategorieAndChefId(chefId);

        // Transform the results into VehicleCategoryStatistics objects
        return results.stream()
                .map(result -> new VehicleCategoryStatistics(
                        result[0].toString(), // Category name
                        ((Number) result[1]).longValue(), // Count of vehicles in this category
                        totalVehicles > 0 ? ((Number) result[1]).longValue() * 100.0 / totalVehicles : 0.0 // Percentage
                ))
                .collect(Collectors.toList());
    }




}
