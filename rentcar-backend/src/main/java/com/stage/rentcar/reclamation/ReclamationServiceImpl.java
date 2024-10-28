package com.stage.rentcar.reclamation;

import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRepository;
import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.agence.AgenceRepository;
import com.stage.rentcar.avis.Avis;
import com.stage.rentcar.avis.AvisRequest;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReclamationServiceImpl implements ReclamationService{
    @Autowired
    private ReclamationRepository reclamationRepository ;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private AgenceRepository agenceRepository;

    @Override
    public ReclamationRequest createReclamation(ReclamationRequest request) {
        Reclamation reclamation = new Reclamation();
        reclamation.setComment(request.getComment());
        reclamation.setArchive(false);

        if (request.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(request.getAgenceId())
                    .orElseThrow(() -> new EntityNotFoundException("agence inexistante avec ID: "));
            reclamation.setAgence(agence);
        } else {
            reclamation.setAgence(null);
        }

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN));
            if (!isAdmin) {
                reclamation.setUser(user);
            } else {
                throw new IllegalArgumentException("User avec ID " + user.getId() + " n'est pas client ou chef.");
            }
        } else {
            throw new EntityNotFoundException("User inexistant avec ID: " + request.getUserId());
        }

        reclamationRepository.save(reclamation);

        return reclamation.getDto();
    }


    @Override
    public ReclamationRequest updateReclamation(Long id, ReclamationRequest request) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" reclamation inexistant: " + id));
        Agence agence = agenceRepository.findById(request.getAgenceId())
                .orElseThrow(() -> new EntityNotFoundException(" agence inexistante: " + id));

        if (request.getComment() != null){
            reclamation.setComment(request.getComment());
        }
          if (request.getAgenceId() != null){
            reclamation.setAgence(agence);
        }

        reclamationRepository.save(reclamation);
        return reclamation.getDto();
    }

    @Override
    public List<ReclamationRequest> getAllReclamations(){
        List<Reclamation> reclamations = reclamationRepository.findAll();
        return reclamations.stream().map(Reclamation::getDto).collect(Collectors.toList());
    }
    @Override
    public List<ReclamationRequest> getAllReclamationsByUser(Integer userId){
        List<Reclamation> reclamations = reclamationRepository.findAllByUserId(userId);
        return reclamations.stream().map(Reclamation::getDto).collect(Collectors.toList());
    }
    @Override
    public void toggleArchive(Long id){
        Reclamation reclamation = reclamationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(" introuvable pour l'id"+id));
        boolean currentStatus = reclamation.getArchive();
        reclamation.setArchive(!currentStatus);
        reclamationRepository.save(reclamation);
    }

    @Override
    public List<ReclamationRequest> getReclamationsByUser(Integer userId){
        List<Reclamation> reclamations = reclamationRepository.findAllByUserId(userId);
        return reclamations.stream().map(Reclamation::getDto).collect(Collectors.toList());
    }

    @Override
    public long countTotalRec() {
        return reclamationRepository.count();
    }

}
