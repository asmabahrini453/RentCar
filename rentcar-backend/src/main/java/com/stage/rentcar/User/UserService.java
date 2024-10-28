package com.stage.rentcar.User;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface UserService {
    void changePassword(Integer id, RequestChangePassword requestChangePassword) ;
    List<UserRequest> getAllClient();
    UserRequest getUserById(Integer id);

    UserRequest getClientById(Integer id);
    UserRequest updateUser (Integer id, UserRequest userRequest);
    void uploadImage(MultipartFile file , Authentication connectedUser, Integer id);
    void toggleArchive(Integer id);
    List<UserRequest> searchClientByNom(String nom) ;
    List<UserRequest> searchChefByNom(String nom);
    List<UserRequest> getAllChefAgence();
    UserRequest getChefAgenceById(Integer id);
    UserRequest createChefAgence(UserRequest userRequest) throws RoleNotFoundException ;

    UserStatisticsRequest getUserStatics();
    List<MonthlyUserStatisticsRequest> getMonthlyClientStatistics();
}
