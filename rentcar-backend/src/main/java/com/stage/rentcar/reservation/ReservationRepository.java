package com.stage.rentcar.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findAllByStatus(Status status);

   List<Reservation> findAllByUserId(Integer userId);

   List<Reservation> findByVehiculeIdIn(List<Integer> vehiculeIds);

    @Query("SELECT r FROM Reservation r WHERE r.dateDepart >= :dDepart AND r.dateRetour <= :dRetour")
    List<Reservation> findReservationsByDateRange(
            @Param("dDepart") LocalDateTime dDepart,
            @Param("dRetour") LocalDateTime dRetour);



    @Query("SELECT r FROM Reservation r WHERE LOWER(r.vehicule.nom) LIKE LOWER(CONCAT('%', :vehiculeNom, '%'))")
    List<Reservation> findByVehiculeNom(@Param("vehiculeNom") String vehiculeNom);
    List<Reservation> findByAgenceId(Integer agenceId);



    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "JOIN r.paiment p " +
            "WHERE p.numFacture = :num OR r.numContrat = :num")
    boolean existsByNumFactureOrNumContrat(@Param("num") Long num);

    List<Reservation> findReservationsByDateRetourBeforeOrDateRetourEquals(LocalDateTime dateRetourBefore, LocalDateTime dateRetourEquals);


    //stat
    @Query("SELECT MONTH(r.dateDepart) as month, SUM(r.montantRes) as totalRevenue " +
            "FROM Reservation r " +
            "WHERE r.dateDepart >= :startDate AND r.dateDepart <= :endDate " +
            "GROUP BY MONTH(r.dateDepart) " +
            "ORDER BY MONTH(r.dateDepart)")
    List<Object[]> findMonthlyRevenue(LocalDateTime startDate, LocalDateTime endDate);

 @Query("SELECT EXTRACT(MONTH FROM r.dateDepart) AS month, SUM(r.montantRes) AS total "
         + "FROM Reservation r "
         + "WHERE r.vehicule.id IN :vehiculeIds "
         + "AND r.dateDepart BETWEEN :startDate AND :endDate "
         + "GROUP BY EXTRACT(MONTH FROM r.dateDepart) "
         + "ORDER BY EXTRACT(MONTH FROM r.dateDepart)")
 List<Object[]> findMonthlyRevenueByChef(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         @Param("vehiculeIds") List<Integer> vehiculeIds);


 @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.id = :chefId AND r.status = :status")
 long countByChefIdAndStatus(@Param("chefId") Integer chefId, @Param("status") Status status);
 @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.id = :chefId")
 long countTotalByChefId(@Param("chefId") Integer chefId);
}




