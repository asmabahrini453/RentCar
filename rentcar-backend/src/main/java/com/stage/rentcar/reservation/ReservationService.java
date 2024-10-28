package com.stage.rentcar.reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    ReservationRequest createReservation (ReservationRequest request) ;
    List<ReservationRequest> getAllReservation();
    List<ReservationRequest> getReservationsByChefId(Integer chefId);
    List<HistoriqueRes> getAllHistoriqueDeRes(Integer userId);
    ReservationRequest getReservationById(Long id);
    List<ReservationRequest> getListResAdmin() ;
    ReservationRequest updateReservation(Long id, ReservationRequest request);
    void updateReservationStatus(Long id, Status updatedStatus);

    void toggleArchive(Long id);
    List<ReservationRequest> findReservationsByDateRange(LocalDateTime dDepart, LocalDateTime dRetour);

    List<ReservationRequest> getReservationsByVehiculeNom(String vehiculeNom);
    List<ReservationRequest> getReservationsByAgenceId(Integer agenceId);
    MontantResponse calculateMontant(MontantRequest montantRequest);
    List<RetourRequest> getReservationsRetours() ;
    void annulerReservation(Long id);
    Double fraisRemboursement(Long id);
    List<RemboursementDto> listFraisRemboursementParReservation();
    Double calculateTotalFraisRemboursement();
    void updateRemboursementStatus(Long reservationId, String remboursementStatus) ;
    List<RetourRequest> getResRetourByChef(Integer chefId);
    void updateRetourResStatus(Long id, RetourStatus updatedStatus, LocalDateTime scheduledDate);

    List<MonthlyRevenueResponse> getMonthlyRevenue(int year) ;
    List<MonthlyRevenueResponse> getMonthlyRevenueByChef(int year, Integer chefId);
    List<StatusPercentageResponse> calculateStatusPercentagesByChefId(Integer chefId);
    Double SommeMontantRes();
    Double SommeNet() ;
}

