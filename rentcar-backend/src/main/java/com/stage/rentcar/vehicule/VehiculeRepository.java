package com.stage.rentcar.vehicule;

import com.stage.rentcar.vehicule.categorie.Categorie;
import com.stage.rentcar.vehicule.categorie.ECategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule,Integer> {
    long count();

    List<Vehicule> findByUserId(Integer userId);

    List<Vehicule> findByScheduledAvailabilityDateNotNull();

    @Query("SELECT v FROM Vehicule v WHERE LOWER(v.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Vehicule> findAllVehiculeByNom(String nom);

    List<Vehicule> findAllByUserId(Integer id);
    List<Vehicule> findByCategorie(Categorie categorie);
    List<Vehicule> findByCategorieAndUserId(Categorie categorie, Integer chefId);
    @Query("SELECT v FROM Vehicule v WHERE LOWER(v.marque.nom) LIKE LOWER(CONCAT('%', :marqueNom, '%'))")
    List<Vehicule> findByMarqueNomContainingIgnoreCase(@Param("marqueNom") String marqueNom);


    @Query("SELECT v FROM Vehicule v WHERE v.prixParJour BETWEEN :minPrix AND :maxPrix AND v.archive = false")
    List<Vehicule> findByPrixParJourBetween(@Param("minPrix") Double minPrix, @Param("maxPrix") Double maxPrix);

    @Query("SELECT v FROM Vehicule v WHERE " +
            "(:nom IS NULL OR LOWER(v.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:marqueNom IS NULL OR LOWER(v.marque.nom) LIKE LOWER(CONCAT('%', :marqueNom, '%'))) AND " +
            "(:categorie IS NULL OR v.categorie.name = :categorie) AND " +
            "(:minPrix IS NULL OR v.prixParJour >= :minPrix) AND " +
            "(:maxPrix IS NULL OR v.prixParJour <= :maxPrix)")
    List<Vehicule> findVehicules(@Param("nom") String nom,
                                 @Param("marqueNom") String marqueNom,
                                 @Param("categorie") ECategorie categorie,
                                 @Param("minPrix") Double minPrix,
                                 @Param("maxPrix") Double maxPrix);




    @Query("SELECT v FROM Vehicule v " +
            "JOIN v.agence a " +
            "JOIN v.categorie c " +
            "LEFT JOIN v.reservations r " +
            "WHERE v.disponibilite = true " +
            "AND a.nom LIKE %:agenceNom% " +
            "AND v.kmSortie >= :kmSortie " +
            "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :categorie, '%')) " +
            "AND NOT EXISTS (SELECT r.id FROM Reservation r WHERE r.vehicule.id = v.id " +
            "AND (r.dateDepart <= :dateRetour AND r.dateRetour >= :dateDepart))")
    List<Vehicule> findAvailableVehicules(
            @Param("agenceNom") String agenceNom,
            @Param("kmSortie") Double kmSortie,
            @Param("categorie") String categorie,
            @Param("dateDepart") LocalDateTime dateDepart,
            @Param("dateRetour") LocalDateTime dateRetour);

    @Query("SELECT v FROM Vehicule v " +
            "JOIN v.categorie c " +
            "LEFT JOIN v.reservations r " +
            "WHERE v.disponibilite = true " +
            "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :categorie, '%')) " +
            "AND NOT EXISTS (SELECT r.id FROM Reservation r WHERE r.vehicule.id = v.id " +
            "AND (r.dateDepart <= :dateRetour AND r.dateRetour >= :dateDepart))")
    List<Vehicule> findAvailableVehiculesByDateRangeAndCategory(
            @Param("categorie") String categorie,
            @Param("dateDepart") LocalDateTime dateDepart,
            @Param("dateRetour") LocalDateTime dateRetour);
  @Query("SELECT v FROM Vehicule v " +
            "LEFT JOIN v.reservations r " +
            "WHERE v.disponibilite = true " +
            "AND NOT EXISTS (SELECT r.id FROM Reservation r WHERE r.vehicule.id = v.id " +
            "AND (r.dateDepart <= :dateRetour AND r.dateRetour >= :dateDepart))")
    List<Vehicule> findAvailableVehiculesByDateRange(
            @Param("dateDepart") LocalDateTime dateDepart,
            @Param("dateRetour") LocalDateTime dateRetour);

    @Query("SELECT v FROM Vehicule v " +
            "JOIN v.marque m " +
            "LEFT JOIN v.reservations r " +
            "WHERE v.disponibilite = true " +
            "AND m.id = :marqueId " +
            "AND NOT EXISTS (SELECT r2.id FROM Reservation r2 WHERE r2.vehicule.id = v.id " +
            "AND (r2.dateDepart <= :dateRetour AND r2.dateRetour >= :dateDepart))")
    List<Vehicule> findAvailableVehiculesByDateRangeAndMarque(
            @Param("marqueId") Integer marqueId,
            @Param("dateDepart") LocalDateTime dateDepart,
            @Param("dateRetour") LocalDateTime dateRetour);

    @Query("SELECT MIN(v.prixParJour) FROM Vehicule v")
    Double findMinPrix();

    @Query("SELECT MAX(v.prixParJour) FROM Vehicule v")
    Double findMaxPrix();

}
