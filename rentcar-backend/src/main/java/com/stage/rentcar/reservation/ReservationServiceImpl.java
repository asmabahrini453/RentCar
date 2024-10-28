package com.stage.rentcar.reservation;

import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRepository;
import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.file.FileUtils;
import com.stage.rentcar.handler.ResourceNotFoundException;
import com.stage.rentcar.optionSupplementaire.OptionPricesRequest;
import com.stage.rentcar.optionSupplementaire.Options;
import com.stage.rentcar.optionSupplementaire.OptionsRepository;
import com.stage.rentcar.optionSupplementaire.OptionsService;
import com.stage.rentcar.paiment.Paiment;
import com.stage.rentcar.paiment.PaimentRepository;
import com.stage.rentcar.permis.Permis;
import com.stage.rentcar.permis.PermisRepository;
import com.stage.rentcar.role.ERole;
import com.stage.rentcar.vehicule.Vehicule;
import com.stage.rentcar.vehicule.VehiculeRepository;
import com.stage.rentcar.vehicule.VehiculeRequest;
import com.stage.rentcar.vehicule.VehiculeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private VehiculeRepository vehiculeRepository;
    @Autowired
    private VehiculeService vehiculeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private PaimentRepository paimentRepository;
    @Autowired
    private PermisRepository permisRepository;
    @Autowired
    private OptionsService optionsService;

    @Override
    public ReservationRequest createReservation(ReservationRequest reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User inexistant avec ID: " + reservationRequest.getUserId()));

        boolean isClient = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_CLIENT));

        if (!isClient) {
            throw new IllegalArgumentException("User avec ID " + user.getId() + " n'est pas un 'client'.");
        }

        Vehicule vehicule = vehiculeRepository.findById(reservationRequest.getVehiculeId())
                .orElseThrow(() -> new RuntimeException("Vehicule introuvable"));
        // Check if the vehicle is available
        if (!vehicule.getDisponibilite()) {
            throw new IllegalArgumentException("Le vehicule avec ID " + vehicule.getId() + " n'est pas disponible.");
        }
        Agence agence = vehicule.getAgence();

        Permis permis = Permis.builder()
                .numero(reservationRequest.getPermis().getNumero())
                .dateObtention(reservationRequest.getPermis().getDateObtention())
                .organisme(reservationRequest.getPermis().getOrganisme())
                .user(user)
                .build();
        permisRepository.save(permis);

        Long uniqueNumber = generateUniqueNumber();

        Paiment paiment = Paiment.builder()
                .methode(reservationRequest.getMethode())
                .numFacture(uniqueNumber)
                .build();
        paimentRepository.save(paiment);

        Status status;
        if (reservationRequest.getMethode() == paiment.getMethode().paypal) {
            status = Status.approuve;
        } else {
            status = Status.en_attente;
        }

        Options options = null;
        if (reservationRequest.getOptions() != null) {
            options = Options.builder()
                    .siegeEnfant(reservationRequest.getOptions().getSiegeEnfant())
                    .nbrSiegeEnfant(reservationRequest.getOptions().getNbrSiegeEnfant())
                    .siegeBebe(reservationRequest.getOptions().getSiegeBebe())
                    .nbrSiegeBebe(reservationRequest.getOptions().getNbrSiegeBebe())
                    .GPS(reservationRequest.getOptions().getGPS())
                    .nbrGPS(reservationRequest.getOptions().getNbrGPS())
                    .prixGPS(reservationRequest.getOptions().getPrixGPS())
                    .assistanceRoutiere(reservationRequest.getOptions().getAssistanceRoutiere())
                    .protectionComplete(reservationRequest.getOptions().getProtectionComplete())
                    .build();
            optionsRepository.save(options);
        }

        Reservation reservation = Reservation.builder()
                .dateDepart(reservationRequest.getDateDepart())
                .dateRetour(reservationRequest.getDateRetour())
                .jours(reservationRequest.getJours())
                .numContrat(uniqueNumber)
                .archive(false)
                .status(status)
                .montantRes(reservationRequest.getMontantRes())
                .options(options)
                .paiment(paiment)
                .permis(permis)
                .vehicule(vehicule)
                .user(user)
                .agence(agence)
                .build();
        reservation = reservationRepository.save(reservation);
        // Set the vehicle's availability to false after the reservation is created
        vehicule.setDisponibilite(false);
        vehiculeRepository.save(vehicule);
        return reservation.getDto();
    }

    @Override
    public MontantResponse calculateMontant(MontantRequest montantRequest) {
        Vehicule vehicule = vehiculeRepository.findById(montantRequest.getVehiculeId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicule not found"));

        Integer agenceId = vehicule.getAgence().getId();
        OptionPricesRequest optionPrices = optionsService.getOptionsPricesByAgenceId(agenceId);

        if (optionPrices == null) {
            throw new EntityNotFoundException("Option prices not found for the given agency");
        }

        Double prixVehicule = vehicule.getPrixParJour();
        Options selectedOptions = montantRequest.getOptions();
        if (selectedOptions == null) {
            selectedOptions = new Options();
        }
        Integer jours = montantRequest.getJours();

        Double prixOptions = 0.0;
        if (selectedOptions.getNbrSiegeEnfant() != null && selectedOptions.getNbrSiegeEnfant() > 0) {
            prixOptions += selectedOptions.getNbrSiegeEnfant() * optionPrices.getPrixSiegeEnfant();
        }
        if (selectedOptions.getNbrSiegeBebe() != null && selectedOptions.getNbrSiegeBebe() > 0) {
            prixOptions += selectedOptions.getNbrSiegeBebe() * optionPrices.getPrixSiegeBebe();
        }
        if (selectedOptions.getNbrGPS() != null && selectedOptions.getNbrGPS() > 0) {
            prixOptions += selectedOptions.getNbrGPS() * optionPrices.getPrixGPS();
        }
        if (selectedOptions.getAssistanceRoutiere() != null && selectedOptions.getAssistanceRoutiere()) {
            prixOptions += optionPrices.getPrixAssistanceRoutiere();
        }
        if (selectedOptions.getProtectionComplete() != null && selectedOptions.getProtectionComplete()) {
            prixOptions += optionPrices.getPrixProtectionComplete();
        }

        Double vehiculeMontant = prixVehicule * jours;
        Double optionMontant = prixOptions * jours;
        Double totalMontant = vehiculeMontant + optionMontant;

        return new MontantResponse(vehiculeMontant, optionMontant, totalMontant);
    }

    @Override
    public List<ReservationRequest> getAllReservation() {
        List<Reservation> reservationList = reservationRepository.findAll();
        return reservationList.stream().map(Reservation::getDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationRequest> getReservationsByChefId(Integer chefId) {
        //1: fetch all vehicules 7asb el chef
        List<VehiculeRequest> vehiculeByChef = vehiculeService.getVehiculeByChefAgence(chefId);
        //2: extract el id mn kol vehicule bch naamlou list of vehiculeIds
        List<Integer> vehiculeIds = vehiculeByChef.stream().map(VehiculeRequest::getId).collect(Collectors.toList());
        //3: fetch res hasb el list of vehiculeIds
        List<Reservation> reservationList = reservationRepository.findByVehiculeIdIn(vehiculeIds);
        return reservationList.stream().map(Reservation::getDto).collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueRes> getAllHistoriqueDeRes(Integer userId) {
        List<Reservation> reservationList = reservationRepository.findAllByUserId(userId);
        return reservationList.stream().map(Reservation::getHistoriqueDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationRequest> getListResAdmin() {
        List<Reservation> reservationList = reservationRepository.findAll();
        return reservationList.stream().map(Reservation::getDto).collect(Collectors.toList());
    }


    @Override
    public ReservationRequest getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        return reservation.getDto();
    }

    @Override
    public ReservationRequest updateReservation(Long id, ReservationRequest request) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();

            if (request.getUserId() != null) {
                System.out.println("Fetching user with id: " + request.getUserId());
                User user = userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("Client inexistant avec ID: " + request.getUserId()));
                System.out.println("Fetched user: " + user);
                boolean isClient = user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(ERole.ROLE_CLIENT));
                if (!isClient) {
                    throw new IllegalArgumentException("User avec ID " + user.getId() + " n'est pas un client.");
                }
                reservation.setUser(user);
            }

            if (request.getVehiculeId() != null) {
                System.out.println("Fetching vehicule with id: " + request.getVehiculeId());
                Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                        .orElseThrow(() -> new EntityNotFoundException("Vehicule inexistant avec ID: " + request.getVehiculeId()));
                System.out.println("Fetched vehicule: " + vehicule);
                reservation.setVehicule(vehicule);
            }

            if (request.getPermis() != null) {
                System.out.println("Updating permis");
                Permis permis = Permis.builder()
                        .numero(request.getPermis().getNumero())
                        .dateObtention(request.getPermis().getDateObtention())
                        .organisme(request.getPermis().getOrganisme())
                        .user(reservation.getUser())
                        .build();
                permisRepository.save(permis);
                System.out.println("Saved permis: " + permis);
                reservation.setPermis(permis);
            }

            if (request.getMethode() != null) {
                System.out.println("Updating paiment");
                Paiment paiment = Paiment.builder()
                        .methode(request.getMethode())
                        .numFacture(request.getNumFacture())
                        .reservation(reservation)
                        .build();

                paimentRepository.save(paiment);
                System.out.println("Saved paiment: " + paiment);
                reservation.setPaiment(paiment);

                if (request.getStatus() != null) {
                    if (request.getMethode() == paiment.getMethode().paypal) {
                        request.setStatus(Status.approuve);
                    } else {
                        request.setStatus(Status.en_attente);
                    }
                }
            }

            if (request.getOptions() != null) {
                System.out.println("Updating options");
                Options options = Options.builder()
                        .siegeEnfant(request.getOptions().getSiegeEnfant())
                        .nbrSiegeEnfant(request.getOptions().getNbrSiegeEnfant())
                        .siegeBebe(request.getOptions().getSiegeBebe())
                        .nbrSiegeBebe(request.getOptions().getNbrSiegeBebe())
                        .GPS(request.getOptions().getGPS())
                        .nbrGPS(request.getOptions().getNbrGPS())
                        .prixGPS(request.getOptions().getPrixGPS())
                        .assistanceRoutiere(request.getOptions().getAssistanceRoutiere())
                        .protectionComplete(request.getOptions().getProtectionComplete())
                        .reservation(reservation)
                        .build();
                optionsRepository.save(options);
                System.out.println("Saved options: " + options);
                reservation.setOptions(options);
            }

            reservation.setDateDepart(request.getDateDepart());
            reservation.setDateRetour(request.getDateRetour());
            reservation.setJours(request.getJours());
            reservation.setNumContrat(request.getNumContrat());

            Reservation updatedReservation = reservationRepository.save(reservation);
            System.out.println("Updated reservation: " + reservation);

            return updatedReservation.getDto();
        } else {
            throw new EntityNotFoundException("Reservation not found with ID: " + id);
        }
    }

    @Override
    public void updateReservationStatus(Long id, Status updatedStatus) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        reservation.setStatus(updatedStatus);
        reservationRepository.save(reservation);
    }


    @Override
    public void toggleArchive(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        boolean currentStatus = reservation.getArchive();
        reservation.setArchive(!currentStatus);
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationRequest> findReservationsByDateRange(LocalDateTime dDepart, LocalDateTime dRetour) {
        List<Reservation> reservationRequests = reservationRepository.findReservationsByDateRange(dDepart, dRetour);
        return reservationRequests.stream().map(Reservation::getDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationRequest> getReservationsByAgenceId(Integer agenceId) {
        List<Reservation> reservationRequests = reservationRepository.findByAgenceId(agenceId);
        return reservationRequests.stream().map(Reservation::getDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationRequest> getReservationsByVehiculeNom(String vehiculeNom) {
        List<Reservation> reservationRequests = reservationRepository.findByVehiculeNom(vehiculeNom);
        return reservationRequests.stream().map(Reservation::getDto).collect(Collectors.toList());
    }

    public Long generateUniqueNumber() {
        Long uniqueNumber;
        do {
            uniqueNumber = Long.valueOf(String.format("%08d", new Random().nextInt(100000000)));
        } while (reservationRepository.existsByNumFactureOrNumContrat(uniqueNumber));
        return uniqueNumber;
    }
    //**************************Annulation**********************************//

    @Override
    public void annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        Vehicule vehicule = vehiculeRepository.findById(reservation.getVehicule().getId()).orElseThrow();
        reservation.setStatus(Status.annule);
        if (reservation.getRemboursementStatus() == null) {
            reservation.setRemboursementStatus(RemboursementStatus.remboursement_incomplet);
            vehicule.setDisponibilite(true);
        }

        reservationRepository.save(reservation);
        vehiculeRepository.save(vehicule);
    }

    @Override
    public Double fraisRemboursement(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        if (reservation.getStatus() == Status.annule) {
            Double fraisRemboursement = reservation.getMontantRes() - reservation.getVehicule().getFraisAnnulation();
            return fraisRemboursement;
        } else {
            return 0.0;
        }
    }

    @Override
    public List<RemboursementDto> listFraisRemboursementParReservation() {
        List<Reservation> reservationsAnnule = reservationRepository.findAllByStatus(Status.annule);
        List<RemboursementDto> remboursementList = new ArrayList<>();

        for (Reservation reservation : reservationsAnnule) {
            Double frais = reservation.getMontantRes();
            if (reservation.getVehicule() != null) {
                frais -= reservation.getVehicule().getFraisAnnulation();
            }
            remboursementList.add(RemboursementDto.builder()
                    .reservationId(reservation.getId())
                    .frais(frais)
                    .remboursementStatus(reservation.getRemboursementStatus())
                    .build());
        }

        return remboursementList;
    }

    @Override
    public Double calculateTotalFraisRemboursement() {
        List<RemboursementDto> fraisRemboursement = listFraisRemboursementParReservation();

        Double totalAmount = fraisRemboursement.stream().mapToDouble(RemboursementDto::getFrais).sum();
        return totalAmount;
    }

    @Override
    public void updateRemboursementStatus(Long reservationId, String remboursementStatus) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable"));
        RemboursementStatus NewRemboursementStatus;
        try {
            NewRemboursementStatus = RemboursementStatus.valueOf(remboursementStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + remboursementStatus);
        }
        reservation.setRemboursementStatus(NewRemboursementStatus);
        reservationRepository.save(reservation);
    }



    //**************************Retour**********************************//

    @Override
    public List<RetourRequest> getReservationsRetours() {
        LocalDateTime now = LocalDateTime.now();

        // Retrieve reservations where the retour date is before or equal to the current date
        List<Reservation> reservations = reservationRepository.findReservationsByDateRetourBeforeOrDateRetourEquals(now, now);
        System.out.println("Number of RetourRequests: " + reservations.size());

        // Iterate over the reservations and update their retourStatus
        reservations.forEach(reservation -> {
            // Check if the retourStatus is already 'retour_complet'; if so, skip updating
            if (reservation.getRetourStatus() != RetourStatus.retour_complet) {
                if (reservation.getDateRetour().isBefore(now)) {
                    // If the current date has passed the retour date, set to 'retour_en_retard'
                    reservation.setRetourStatus(RetourStatus.retour_en_retard);
                } else {
                    // Otherwise, set to 'en_cours'
                    reservation.setRetourStatus(RetourStatus.en_cours);
                }
                // Save the updated reservation
                reservationRepository.save(reservation);
            }
        });

        // Convert the reservations to RetourRequest DTOs and return them
        return reservations.stream()
                .map(Reservation::getRetourDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RetourRequest> getResRetourByChef(Integer chefId) {
        // Step 1: Fetch all vehicles managed by the given chef
        List<VehiculeRequest> vehiculeByChef = vehiculeService.getVehiculeByChefAgence(chefId);

        // Step 2: Extract vehicle IDs to create a list of vehiculeIds
        List<Integer> vehiculeIds = vehiculeByChef.stream()
                .map(VehiculeRequest::getId)
                .collect(Collectors.toList());

        // Step 3: Fetch reservations for the list of vehicle IDs
        List<Reservation> reservationList = reservationRepository.findByVehiculeIdIn(vehiculeIds);

        LocalDateTime now = LocalDateTime.now();

        // Step 4: Iterate over the reservations and update their retourStatus
        reservationList.forEach(reservation -> {
            // Check if the retourStatus is already 'retour_complet'; if so, skip updating
            if (reservation.getRetourStatus() != RetourStatus.retour_complet) {
                if (reservation.getDateRetour().isBefore(now)) {
                    // If the current date has passed the retour date, set to 'retour_en_retard'
                    reservation.setRetourStatus(RetourStatus.retour_en_retard);
                } else {
                    // Otherwise, set to 'en_cours'
                    reservation.setRetourStatus(RetourStatus.en_cours);
                }
                // Save the updated reservation
                reservationRepository.save(reservation);
            }
        });

        // Convert the reservations to RetourRequest DTOs and return them
        return reservationList.stream()
                .map(Reservation::getRetourDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRetourResStatus(Long id, RetourStatus updatedStatus, LocalDateTime scheduledDate) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation introuvable"));

        Vehicule vehicule = vehiculeRepository.findById(reservation.getVehicule().getId())
                .orElseThrow(() -> new RuntimeException("Vehicule introuvable"));

        reservation.setRetourStatus(updatedStatus);

        if (updatedStatus == RetourStatus.retour_complet) {
            if (scheduledDate != null && scheduledDate.isAfter(LocalDateTime.now())) {
                // Schedule the task to update availability
                scheduleVehicleAvailabilityUpdate(vehicule, scheduledDate);
            } else {
                vehicule.setDisponibilite(true);
            }
        } else if (updatedStatus == RetourStatus.retour_en_retard || updatedStatus == RetourStatus.en_cours) {
            vehicule.setDisponibilite(false);
        }

        reservationRepository.save(reservation);
        vehiculeRepository.save(vehicule);
    }

    // Method to schedule vehicle availability update
    @Async
    public void scheduleVehicleAvailabilityUpdate(Vehicule vehicule, LocalDateTime scheduledDate) {
        try {
            // Calculate the delay
            long delay = scheduledDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

            // Wait until the scheduled date
            if (delay > 0) {
                Thread.sleep(delay); // This is a simple approach, you may want to use a more robust method
            }

            // Update the availability
            vehicule.setDisponibilite(true);
            vehiculeRepository.save(vehicule);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error scheduling availability update", e);
        }
    }

    @Override
    public Double SommeMontantRes() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(Reservation::getMontantRes)
                .reduce(0.0, Double::sum);
    }
  @Override
    public Double SommeNet() {
        List<Reservation> reservations = reservationRepository.findAll();
       Double somme=  reservations.stream()
                .map(Reservation::getMontantRes)
                .reduce(0.0, Double::sum);
       Double S= somme- calculateTotalFraisRemboursement();
       return S ;
    }



    //stat
@Override
public List<MonthlyRevenueResponse> getMonthlyRevenue(int year) {
    LocalDateTime startDate = YearMonth.of(year, 1).atDay(1).atStartOfDay();
    LocalDateTime endDate = YearMonth.of(year, 12).atEndOfMonth().atTime(23, 59, 59);

    List<Object[]> results = reservationRepository.findMonthlyRevenue(startDate, endDate);
    Map<Integer, Double> revenueMap = results.stream()
            .collect(Collectors.toMap(
                    result -> (Integer) result[0],
                    result -> (Double) result[1]   // Total
            ));

    List<MonthlyRevenueResponse> monthlyRevenues = new ArrayList<>();
    for (int month = 1; month <= 12; month++) {
        Double totalRevenue = revenueMap.getOrDefault(month, 0.0);
        LocalDate mois = LocalDate.of(year, month, 1);
        monthlyRevenues.add(new MonthlyRevenueResponse(mois, totalRevenue));
    }

    return monthlyRevenues;
}


    @Override
    public List<MonthlyRevenueResponse> getMonthlyRevenueByChef(int year, Integer chefId) {
        // 1. Fetch all vehicles managed by the chef
        List<VehiculeRequest> vehiculeByChef = vehiculeService.getVehiculeByChefAgence(chefId);

        // 2. Extract vehicle IDs from the list
        List<Integer> vehiculeIds = vehiculeByChef.stream()
                .map(VehiculeRequest::getId)
                .collect(Collectors.toList());

        // 3. Define start and end dates for the year
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        // 4. Fetch monthly revenue based on vehicle IDs and date range
        List<Object[]> results = reservationRepository.findMonthlyRevenueByChef(startDate, endDate, vehiculeIds);

        // 5. Map results to a revenue map
        Map<Integer, Double> revenueMap = results.stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> (Double) result[1]
                ));

        // 6. Prepare the monthly revenue response
        List<MonthlyRevenueResponse> monthlyRevenues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Double totalRevenue = revenueMap.getOrDefault(month, 0.0);
            LocalDate mois = LocalDate.of(year, month, 1);
            monthlyRevenues.add(new MonthlyRevenueResponse(mois, totalRevenue));
        }

        return monthlyRevenues;
    }
    @Override
    public List<StatusPercentageResponse> calculateStatusPercentagesByChefId(Integer chefId) {
        // Fetch all reservations for the chef
        List<ReservationRequest> reservations = getReservationsByChefId(chefId);

        // Total reservations for the chef
        long totalReservations = reservations.size();
        System.out.println("Total reservations for chefId " + chefId + ": " + totalReservations);

        List<StatusPercentageResponse> percentages = new ArrayList<>();

        // Count for each status
        long countEnAttente = reservations.stream().filter(r -> r.getStatus() == Status.en_attente).count();
        long countApprouve = reservations.stream().filter(r -> r.getStatus() == Status.approuve).count();
        long countAnnule = reservations.stream().filter(r -> r.getStatus() == Status.annule).count();

        // Log the counts
        System.out.println("Count for chefId " + chefId + " with status en_attente: " + countEnAttente);
        System.out.println("Count for chefId " + chefId + " with status approuve: " + countApprouve);
        System.out.println("Count for chefId " + chefId + " with status annule: " + countAnnule);

        // Calculate percentages
        double percentageEnAttente = totalReservations > 0 ? ((double) countEnAttente / totalReservations) * 100 : 0.0;
        double percentageApprouve = totalReservations > 0 ? ((double) countApprouve / totalReservations) * 100 : 0.0;
        double percentageAnnule = totalReservations > 0 ? ((double) countAnnule / totalReservations) * 100 : 0.0;

        // Prepare the response
        percentages.add(new StatusPercentageResponse(Status.en_attente, percentageEnAttente));
        percentages.add(new StatusPercentageResponse(Status.approuve, percentageApprouve));
        percentages.add(new StatusPercentageResponse(Status.annule, percentageAnnule));

        return percentages;
    }

}

