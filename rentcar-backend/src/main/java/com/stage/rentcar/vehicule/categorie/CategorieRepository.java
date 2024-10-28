package com.stage.rentcar.vehicule.categorie;

import com.stage.rentcar.role.ERole;
import com.stage.rentcar.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie,Integer> {
    Categorie findByName(ECategorie name);

    @Query("SELECT c.name AS category, COUNT(v) AS count " +
            "FROM Vehicule v JOIN v.categorie c " +
            "GROUP BY c.name")
    List<Object[]> countVehiclesByCategorie();

    @Query("SELECT c.name, COUNT(v) FROM Vehicule v JOIN v.categorie c WHERE v.user.id = :chefId GROUP BY c.name")
    List<Object[]> countVehiclesByCategorieAndChefId(@Param("chefId") Long chefId);

}
