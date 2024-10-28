package com.stage.rentcar.agence;

import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRepository;
import com.stage.rentcar.file.FileStorageService;
import com.stage.rentcar.api.NominatimService;
import com.stage.rentcar.role.ERole;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgenceServiceImpl implements AgenceService{
    private final AgenceRepository agenceRepository ;
    private final UserRepository userRepository ;
    private final FileStorageService fileStorageService ;
    private final NominatimService nominatimService;

    @Override
    public List<AgenceRequest> getAllAgence(){
        List<Agence> agences = agenceRepository.findAll();
        return agences.stream().map(Agence::getDto).collect(Collectors.toList());
    }

    @Override
    public  AgenceRequest getAgenceById (Integer id){
        Agence agence = agenceRepository.findById(id).orElseThrow(()->new EntityNotFoundException("l'agence n'existe pas"));
        return agence.getDto();
    }

    @Override
    public List<AgenceRequest> getAgenceByChefAgence(Integer userId){
        User user = userRepository.findByIdAndRoles_Name(userId , ERole.ROLE_CHEF_AGENCE)
                .orElseThrow(()->new EntityNotFoundException("chef dagence inexistant"));
        List<Agence> agences = agenceRepository.findAllByUserId(userId);
        if (agences.isEmpty()) {
            throw new EntityNotFoundException("Aucune agence trouvée pour ce chef d'agence");
        }
        return agences.stream().map(Agence::getDto).collect(Collectors.toList());
    }
    @Override
    public AgenceRequest createAgence(AgenceRequest agenceRequest) {

        Agence agence = new Agence();
        agence.setNom(agenceRequest.getNom());
        agence.setDteCreation(agenceRequest.getDteCreation());
        agence.setHeureOuverture(agenceRequest.getHeureOuverture());
        agence.setHeureFermeture(agenceRequest.getHeureFermeture());
        agence.setArchive(false);

        if (agenceRequest.getLocalisation() != null) {
            String displayName = nominatimService.getDisplayName(agenceRequest.getLocalisation());
            System.out.println("Display Name: " + displayName);
            agence.setLocalisation(displayName);
        }
        Optional<User> userOptional = userRepository.findById(agenceRequest.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // lezem user ykoun de role 'chef agence'
            boolean isChefAgence = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ROLE_CHEF_AGENCE));

            if (isChefAgence) {
                agence.setUser(user);
            } else {
                throw new IllegalArgumentException("User avec ID " + user.getId() + " n'est pas un 'chef agence'.");
            }
        } else {
            throw new EntityNotFoundException("User inexistant avec ID: " + agenceRequest.getUserId());
        }


        agenceRepository.save(agence);

        return agence.getDto();
    }

    @Override
    public AgenceRequest updateAgence(Integer id, AgenceRequest agenceRequest)  {
        Optional<Agence> agence = agenceRepository.findById(id);

        if (agence.isPresent()) {
            Agence updatedAgence = agence.get();

            if (agenceRequest.getNom() != null){
                updatedAgence.setNom(agenceRequest.getNom());
            }
            if (agenceRequest.getDteCreation() != null){
                updatedAgence.setDteCreation(agenceRequest.getDteCreation());
            }
            if (agenceRequest.getHeureOuverture() != null){
                updatedAgence.setHeureOuverture(agenceRequest.getHeureOuverture());
            }
            if (agenceRequest.getHeureFermeture() != null){
                updatedAgence.setHeureFermeture(agenceRequest.getHeureFermeture());
            }
            if (agenceRequest.getLocalisation() != null) {
                String displayName = nominatimService.getDisplayName(agenceRequest.getLocalisation());
                System.out.println("Display Name: " + displayName);
                updatedAgence.setLocalisation(displayName);
            }

            if (agenceRequest.getUserId() != null) {
                Optional<User> userOptional = userRepository.findById(agenceRequest.getUserId());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    boolean isChefAgence = user.getRoles().stream()
                            .anyMatch(role -> role.getName().equals(ERole.ROLE_CHEF_AGENCE));

                    if (isChefAgence) {
                        updatedAgence.setUser(user);
                    } else {
                        throw new IllegalArgumentException("User avec cette ID " + user.getId() + " n'est pas un chef agence.");
                    }
                } else {
                    throw new EntityNotFoundException("User inexistant avec cette ID: " + agenceRequest.getUserId());
                }
            }
            updatedAgence.setArchive(false);
            Agence savedAgence = agenceRepository.save(updatedAgence);
            return savedAgence.getDto();
        } else {
            throw new EntityNotFoundException("agence inexistant");
        }
    }
    @Override
    public void uploadAgenceLogo(MultipartFile file, Authentication connectedUser, Integer id){
        Agence agence = agenceRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("agence intrrouvable"));
        User user = ((User) connectedUser.getPrincipal());
        //for each user i will create a folder where i upload all files that belongs to that specefic user
        var agenceLogo = fileStorageService.saveFile(file,user.getId());
        agence.setAgenceLogo(agenceLogo);
        agenceRepository.save(agence);

    }

    @Override
    public void toggleArchive(Integer id){
        Agence agence = agenceRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("agence introuvable pour l'id"+id));
        boolean currentStatus = agence.getArchive();
        agence.setArchive(!currentStatus);
        agenceRepository.save(agence);
    }


    @Override
    public List<AgenceRequest> SearchAgenceByNom(String nom){
        List<Agence> agence = agenceRepository.findAllAgenceByNom(nom) ;
        return agence.stream().map(Agence::getDto).collect(Collectors.toList());
    }
    @Override
  public List<AgenceRequest> searchAgenceByLocalisation(String localisation){
        List<Agence> agence = agenceRepository.findAllAgenceByLocalisation(localisation) ;
        return agence.stream().map(Agence::getDto).collect(Collectors.toList());
    }
    @Override
    public long countTotalAgences() {
        return agenceRepository.count();
    }

    @Override
    public long countTotalAgencesByChefId(Integer chefId) {
        return agenceRepository.countByUserId(chefId);
    }




}
