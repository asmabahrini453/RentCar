package com.stage.rentcar.vehicule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityScheduler {

    private final VehiculeRepository vehiculeRepository;

    public AvailabilityScheduler(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    @Scheduled(fixedRate = 60000) // kol min
    public void updateVehiculeAvailability() {
        List<Vehicule> vehicles = vehiculeRepository.findByScheduledAvailabilityDateNotNull();

        for (Vehicule vehicule : vehicles) {
            if (vehicule.getScheduledAvailabilityDate().isBefore(LocalDateTime.now())) {
                vehicule.setDisponibilite(true);
                vehicule.setScheduledAvailabilityDate(null);
                vehiculeRepository.save(vehicule);
            }
        }
    }
}
