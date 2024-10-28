package com.stage.rentcar.User;

import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.role.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Integer> {
 Optional<User> findByEmail(String email) ;
 List<User> findByNomAndRoles_Name(String nom, ERole roleName) ;
 Optional<User> findByIdAndRoles_Name(Integer userId, ERole roleName);
 List<User> findAllByRoles_Name(ERole roleName);

    @Query("SELECT u FROM User u JOIN u.roles r  WHERE LOWER(u.nom) LIKE LOWER(CONCAT('%', :nom, '%')) AND r.name = 'ROLE_CHEF_AGENCE'")
    List<User> findAllChefByNom(@Param("nom") String nom);
  @Query("SELECT u FROM User u JOIN u.roles r  WHERE LOWER(u.nom) LIKE LOWER(CONCAT('%', :nom, '%')) AND r.name = 'ROLE_CLIENT'")
    List<User> findAllClientByNom(@Param("nom") String nom);



    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :role")
    long countByRole(@Param("role") ERole role);

    @Query("SELECT FUNCTION('DATE_TRUNC', 'month', u.createdAt) AS month, COUNT(u) AS count " +
            "FROM User u JOIN u.roles r " +
            "WHERE r.name = 'ROLE_CLIENT' " +
            "GROUP BY FUNCTION('DATE_TRUNC', 'month', u.createdAt) " +
            "ORDER BY month ASC")
    List<Object[]> countNewClientsByMonth();


}


