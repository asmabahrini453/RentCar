package com.stage.rentcar.avis;

import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRepository;
import com.stage.rentcar.reclamation.Reclamation;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.vehicule.Vehicule;
import com.stage.rentcar.vehicule.VehiculeRepository;
import com.stage.rentcar.vehicule.VehiculeRequest;
import com.stage.rentcar.vehicule.VehiculeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvisServiceImpl  implements AvisService{
    @Autowired
    private AvisRepository avisRepository ;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private VehiculeRepository vehiculeRepository ;
    @Autowired
    private VehiculeService vehiculeService ;

    @Override
    public AvisRequest createAvis(AvisRequest avisRequest) {
        Avis avis = new Avis();
        avis.setComment(avisRequest.getComment());
        avis.setType(avisRequest.getType());
        avis.setRating(avisRequest.getRating());
        avis.setArchive(false);

        if (avisRequest.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(avisRequest.getVehiculeId())
                    .orElseThrow(() -> new EntityNotFoundException("Vehicule inexistant avec ID: " + avisRequest.getVehiculeId()));
            avis.setVehicule(vehicule);
        } else {
            avis.setVehicule(null);
        }

        Optional<User> userOptional = userRepository.findById(avisRequest.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isClient = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ROLE_CLIENT));
            if (isClient) {
                avis.setUser(user);
            } else {
                throw new IllegalArgumentException("User avec ID " + user.getId() + " n'est pas client.");
            }
        } else {
            throw new EntityNotFoundException("User inexistant avec ID: " + avisRequest.getUserId());
        }

        avisRepository.save(avis);

        return avis.getDto();
    }


    @Override
    public List<AvisRequest> getAvisByVehicule(Integer vehiculeId){
        List<Avis> avis = avisRepository.findByVehiculeId(vehiculeId);
        return avis.stream().map(Avis::getDto).collect(Collectors.toList());
    }

    @Override
    public List<AvisRequest> getAvisVehiculeByChefId(Integer chefId) {
        User chef = userRepository.findById(chefId).orElseThrow();
        List<VehiculeRequest> vehicules = vehiculeService.getVehiculeByChefAgence(chefId);

        List<Integer> vehiculeIds = vehicules.stream()
                .map(VehiculeRequest::getId)
                .collect(Collectors.toList());

        List<Avis> avis = new ArrayList<>();
        for (Integer vehiculeId : vehiculeIds) {
            avis.addAll(avisRepository.findByVehiculeId(vehiculeId));
        }

        return avis.stream()
                .map(Avis::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AvisRequest> getAvisByClient(Integer userId){
        List<Avis> avis = avisRepository.findByUserId(userId);
        return avis.stream().map(Avis::getDto).collect(Collectors.toList());
    }

    @Override
    public List<AvisRequest> getAvisByType(AvisType avisType){
        List<Avis> avis = avisRepository.findByType(avisType);
        return avis.stream().map(Avis::getDto).collect(Collectors.toList());
    }


    @Override
    public AvisRequest updateAvis(Long avisId, AvisRequest avisRequest) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new EntityNotFoundException("Avis inexistant avec ID: " + avisId));

        if (avisRequest.getComment() != null){
            avis.setComment(avisRequest.getComment());
        }
        if (avisRequest.getRating() != null){
            avis.setRating(avisRequest.getRating());
        }

        avisRepository.save(avis);
        return avis.getDto();
    }


    @Override
    public void toggleArchive(Long id){
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avis inexistant avec ID: " + id));
        boolean currentStatus = avis.getArchive();
        avis.setArchive(!currentStatus);
        avisRepository.save(avis);
    }
    @Override
    public long countTotalAvis() {
        return avisRepository.count();
    }
    @Override
    public long countTotalAvisByType(AvisType type) {
        return avisRepository.countByType(type);
    }

    @Override
    public long countTotalAvisByChefId(Integer chefId) {
        List<Vehicule> vehicules = vehiculeRepository.findByUserId(chefId);
        List<Integer> vehiculeIds = vehicules.stream()
                .map(Vehicule::getId)
                .collect(Collectors.toList());

        return avisRepository.countByVehiculeIdIn(vehiculeIds);
    }

}
