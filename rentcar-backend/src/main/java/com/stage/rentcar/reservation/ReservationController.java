package com.stage.rentcar.reservation;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("reservation")
@AllArgsConstructor
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ReservationRequest> createReservation(@RequestBody ReservationRequest reservationRequest) {
        ReservationRequest reservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationRequest>> getAllReservation() {
        List<ReservationRequest> reservationList = reservationService.getAllReservation();
        return ResponseEntity.ok().body(reservationList);
    }

    @GetMapping("/historique/{userId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<HistoriqueRes>> getHistoriqueByUserId(@PathVariable Integer userId) {
        List<HistoriqueRes> historiqueList = reservationService.getAllHistoriqueDeRes(userId);
        return ResponseEntity.ok(historiqueList);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationRequest>> getResListAdmin() {
        List<ReservationRequest> list = reservationService.getListResAdmin();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/res-chef/{chefId}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<ReservationRequest>> getResListChef(@PathVariable Integer chefId) {
        List<ReservationRequest> list = reservationService.getReservationsByChefId(chefId);
        return ResponseEntity.ok(list);
    }


    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<ReservationRequest> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<ReservationRequest> updateReservation(@PathVariable Long id, @RequestBody ReservationRequest reservationRequest) {
        ReservationRequest res = reservationService.updateReservation(id, reservationRequest);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/{id}/update-status")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') ")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status) {
        reservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/archive/{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') ")
    public ResponseEntity<Void> archiveReservation(
            @PathVariable Long id
    ) {
        reservationService.toggleArchive(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/search-date")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationRequest>> searchReservationsByDateRange(
            @RequestParam("dDepart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dDepart,
            @RequestParam("dRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dRetour) {

        List<ReservationRequest> reservations = reservationService.findReservationsByDateRange(dDepart, dRetour);
        return ResponseEntity.ok(reservations);
    }


    @GetMapping("/search-par-vehicule")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationRequest>> searchByVehiculeNom(@RequestParam("vehiculeNom") String vehiculeNom) {
        List<ReservationRequest> reservations = reservationService.getReservationsByVehiculeNom(vehiculeNom);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/search-par-agence")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationRequest>> searchByAgenceId(@RequestParam("agenceId") Integer agenceId) {
        List<ReservationRequest> reservations = reservationService.getReservationsByAgenceId(agenceId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/calculate-montant")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MontantResponse> calculateMontant(@RequestBody MontantRequest montantRequest) {
        MontantResponse montantResponse = reservationService.calculateMontant(montantRequest);
        return ResponseEntity.ok(montantResponse);
    }

    //ANNULATION
    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long id) {
        reservationService.annulerReservation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate-remboursement/{id}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Double> calculeFraisRemboursement(@PathVariable Long id) {
        Double montantResponse = reservationService.fraisRemboursement(id);
        return ResponseEntity.ok(montantResponse);
    }

    @GetMapping("/list-remboursement")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RemboursementDto>> listFraisRemboursementParReservation() {
        List<RemboursementDto> remboursementList = reservationService.listFraisRemboursementParReservation();
        return ResponseEntity.ok(remboursementList);
    }

    @GetMapping("/total-remboursement")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Double> calculateTotalFraisRemboursement() {
        Double totalAmount = reservationService.calculateTotalFraisRemboursement();
        return ResponseEntity.ok(totalAmount);
    }

    @PutMapping("/remboursement-status/{id}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') ")
    public ResponseEntity<Void> updateRemboursementStatus(@PathVariable Long id, @RequestParam String remboursementstatus) {
        reservationService.updateRemboursementStatus(id, remboursementstatus);
        return ResponseEntity.ok().build();
    }


    //RETOUR

    @GetMapping("/retour")
    @PreAuthorize(" hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RetourRequest>> getReservationsRetour() {
        List<RetourRequest> retour = reservationService.getReservationsRetours();
        return ResponseEntity.ok(retour);
    }

    @GetMapping("/retour-chef/{chefId}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<RetourRequest>> getReservationsRetourParChef(@PathVariable Integer chefId) {
        List<RetourRequest> list = reservationService.getResRetourByChef(chefId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update-retour-status/{id}")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<Void> updateRetourStatus(
            @PathVariable Long id,
            @RequestParam RetourStatus status,
            @RequestParam(required = false) LocalDateTime scheduledAvailabilityDate) {
        reservationService.updateRetourResStatus(id, status, scheduledAvailabilityDate);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/somme-montant")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public Double getSommeMontantRes() {
        return reservationService.SommeMontantRes();
    }

    @GetMapping("/somme-net")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public Double getSommeNet() {
        return reservationService.SommeNet();
    }
    //stat
    @GetMapping("/stat/{year}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<List<MonthlyRevenueResponse>> getMonthlyRevenue(@PathVariable int year) {
        List<MonthlyRevenueResponse> revenue = reservationService.getMonthlyRevenue(year);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/stat-chef/{chefId}/{year}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<MonthlyRevenueResponse>> getMonthlyRevenueByChef(
            @PathVariable Integer chefId,
            @PathVariable int year) {

        // Calculate start and end date for the year
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<MonthlyRevenueResponse> monthlyRevenues = reservationService.getMonthlyRevenueByChef(year, chefId);

        return ResponseEntity.ok(monthlyRevenues);
    }
    @GetMapping("/status-percentages/{chefId}")
    @PreAuthorize(" hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<List<StatusPercentageResponse>> getStatusPercentagesByChefId(@PathVariable Integer chefId) {
        List<StatusPercentageResponse> percentages = reservationService.calculateStatusPercentagesByChefId(chefId);
        return ResponseEntity.ok(percentages);
    }


}


