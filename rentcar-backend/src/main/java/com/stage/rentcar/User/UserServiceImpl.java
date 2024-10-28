package com.stage.rentcar.User;

import com.stage.rentcar.api.NominatimService;
import com.stage.rentcar.file.FileStorageService;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.role.RoleFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private PasswordEncoder passwordEncoder ;
    @Autowired
    private NominatimService nominatimService ;
    @Autowired
    private FileStorageService fileStorageService ;

    @Autowired
    private RoleFactory roleFactory ;

    @Override
    public List<UserRequest> getAllClient(){
        List<User> client = userRepository.findAllByRoles_Name(ERole.ROLE_CLIENT);
        return client.stream().map(User::getDto).collect(Collectors.toList());

    }

    @Override
    public UserRequest getClientById(Integer id){
        User client = userRepository.findByIdAndRoles_Name(id , ERole.ROLE_CLIENT).orElseThrow(()->
                new EntityNotFoundException("client inexistant pour cet id"+ id));
        return client.getDto();
    }
    @Override
    public UserRequest getUserById(Integer id){
        User client = userRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("user inexistant pour cet id"+ id));
        return client.getUserDto();
    }

    @Override
    public List<UserRequest> getAllChefAgence(){
        List<User> chefs = userRepository.findAllByRoles_Name(ERole.ROLE_CHEF_AGENCE);
        return chefs.stream().map(User::getDto).collect(Collectors.toList());

    }

    @Override
    public UserRequest getChefAgenceById(Integer id){
        User chef = userRepository.findByIdAndRoles_Name(id , ERole.ROLE_CHEF_AGENCE).orElseThrow(()->
                new EntityNotFoundException("chef d'agence inexistant pour cet id"+ id));
        return chef.getDto();
    }
    @Override
    public UserRequest createChefAgence(UserRequest userRequest) throws RoleNotFoundException {
        User chef = new User();
        chef.setNom(userRequest.getNom());
        chef.setPrenom(userRequest.getPrenom());
        chef.setEmail(userRequest.getEmail());
        chef.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        chef.setDateNaiss(userRequest.getDateNaiss());
        chef.setGenre(userRequest.getGenre());
        chef.setTel(userRequest.getTel());
        chef.setCin(userRequest.getCin());
        chef.setArchive(false);
        chef.setRoles(Set.of(roleFactory.getInstance("chef_agence")));
        chef.setEnabled(true);
        chef.setAccountLocked(false);
        if(userRequest.getAdresse() !=null){
            String displayName = nominatimService.getDisplayName(userRequest.getAdresse());
            chef.setAdresse(displayName);
        }

        User savedUser = userRepository.save(chef);

        return savedUser.getDto();

    }


    @Override
    public UserRequest updateUser (Integer id, UserRequest userRequest){
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User updatedUser = user.get();

            if (userRequest.getNom() != null){
                updatedUser.setNom(userRequest.getNom());
            }

            if (userRequest.getPrenom() != null){
                updatedUser.setPrenom(userRequest.getPrenom());
            }
            if (userRequest.getDateNaiss() != null){
                updatedUser.setDateNaiss(userRequest.getDateNaiss());
            }
            if (userRequest.getEmail() != null){
                updatedUser.setEmail(userRequest.getEmail());
            }
            if (userRequest.getTel() != null){
                updatedUser.setTel(userRequest.getTel());
            }
            if (userRequest.getCin() != null){
                updatedUser.setCin(userRequest.getCin());
            }
            if (userRequest.getGenre() != null){
                updatedUser.setGenre(userRequest.getGenre());
            }
            if (userRequest.getAdresse()!= null){
                String displayName = nominatimService.getDisplayName(userRequest.getAdresse());
                updatedUser.setAdresse(displayName);
            }

            User savedUser = userRepository.save(updatedUser);
            return savedUser.getUpdateDto();
        } else {
            throw new EntityNotFoundException("user inexistant");
        }
    }
    @Override
    public void changePassword(Integer id, RequestChangePassword requestChangePassword) {
        User user = userRepository.findById(id).orElseThrow();
        if(!passwordEncoder.matches(requestChangePassword.getOldPassword() , user.getPassword())){
            throw new RuntimeException();
        }
        user.setPassword((passwordEncoder.encode(requestChangePassword.getNewPassword())));
        userRepository.save(user);
    }


    public void toggleArchive(Integer id){
        User user = userRepository.findById(id).orElseThrow();
        boolean currentStatus = user.getArchive();
        user.setArchive(!currentStatus);
        userRepository.save(user);
    }

    public List<UserRequest> searchClientByNom(String nom) {
        List<User> client = userRepository.findAllClientByNom(nom);
        return client.stream().map(User::getDto).collect(Collectors.toList());
    }
    @Override
    public List<UserRequest> searchChefByNom(String nom) {
        List<User> chefs = userRepository.findAllChefByNom(nom);
        return chefs.stream().map(User::getDto).collect(Collectors.toList());
    }

    @Override
    public void uploadImage(MultipartFile file , Authentication connectedUser, Integer id){
        User u = userRepository.findById(id).orElseThrow();
        User user = ((User) connectedUser.getPrincipal());
        var image= fileStorageService.saveFile(file, user.getId());
        u.setImage(image);
        userRepository.save(u);
    }

    //STATISTIQUES

    @Override
    public UserStatisticsRequest getUserStatics(){
        long totalUsers = userRepository.count();
        long clientNbr = userRepository.countByRole(ERole.ROLE_CLIENT);
        long chefAgenceNbr = userRepository.countByRole(ERole.ROLE_CHEF_AGENCE);
    return new UserStatisticsRequest(totalUsers,clientNbr,chefAgenceNbr);

    }

    @Override
    public List<MonthlyUserStatisticsRequest> getMonthlyClientStatistics() {
        List<Object[]> results = userRepository.countNewClientsByMonth();

        return results.stream()
                .map(result -> {
                    LocalDateTime dateTime = (LocalDateTime) result[0];
                    LocalDate date = dateTime.toLocalDate();
                    Long count = ((Number) result[1]).longValue();
                    return new MonthlyUserStatisticsRequest(date, count);
                })
                .collect(Collectors.toList());
    }


}
