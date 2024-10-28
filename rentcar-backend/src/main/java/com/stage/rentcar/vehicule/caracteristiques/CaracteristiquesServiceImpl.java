package com.stage.rentcar.vehicule.caracteristiques;

import com.stage.rentcar.vehicule.VehiculeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaracteristiquesServiceImpl implements CaracteristiquesService {

    @Autowired
    private CaracteristiquesRepository caracteristiquesRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Override
    public CaracteristiquesRequest createCaracteristique(CaracteristiquesRequest request) {
        Caracteristiques caracteristiques = new Caracteristiques();
        caracteristiques.setVitesse(request.getVitesse());
        caracteristiques.setCarburant(request.getCarburant());
        caracteristiques.setCd(request.getCd());
        caracteristiques.setMp3(request.getMp3());
        caracteristiques.setGps(request.getGps());
        caracteristiques.setClimatisation(request.getClimatisation());
        caracteristiques.setPackElectrique(request.getPackElectrique());
        caracteristiques.setRadio(request.getRadio());
        caracteristiques.setRegulateurVitesse(request.getRegulateurVitesse());

        Caracteristiques savedCaracteristiques = caracteristiquesRepository.save(caracteristiques);
        return savedCaracteristiques.getDto();
    }

    @Override
    public List<CaracteristiquesRequest> getAllCaracteristiques() {
        List<Caracteristiques> requests = caracteristiquesRepository.findAll();
        return requests.stream().map(Caracteristiques::getDto).collect(Collectors.toList());
    }

    @Override
    public CaracteristiquesRequest updateCaracteristiques(Integer id, CaracteristiquesRequest request) {
        Optional<Caracteristiques> optionalCaracteristiques = caracteristiquesRepository.findById(id);
        if (optionalCaracteristiques.isPresent()) {
            Caracteristiques caracteristiques = optionalCaracteristiques.get();

            if (request.getVitesse() != null) {
                caracteristiques.setVitesse(request.getVitesse());
            }
            if (request.getCarburant() != null) {
                caracteristiques.setCarburant(request.getCarburant());
            }
            if (request.getRadio() != null) {
                caracteristiques.setRadio(request.getRadio());
            }
            if (request.getGps() != null) {
                caracteristiques.setGps(request.getGps());
            }
            if (request.getCd() != null) {
                caracteristiques.setCd(request.getCd());
            }
            if (request.getMp3() != null) {
                caracteristiques.setMp3(request.getMp3());
            }
            if (request.getClimatisation() != null) {
                caracteristiques.setClimatisation(request.getClimatisation());
            }
            if (request.getPackElectrique() != null) {
                caracteristiques.setPackElectrique(request.getPackElectrique());
            }
            if (request.getRegulateurVitesse() != null) {
                caracteristiques.setRegulateurVitesse(request.getRegulateurVitesse());
            }
 if (request.getNbrPersonnes() != null) {
                caracteristiques.setNbrPersonnes(request.getNbrPersonnes());
            }
 if (request.getDimensionDeChargement() != null) {
                caracteristiques.setDimensionDeChargement(request.getDimensionDeChargement());
            }
 if (request.getChargeUtile() != null) {
                caracteristiques.setChargeUtile(request.getChargeUtile());
            }

            Caracteristiques updatedCaracteristiques = caracteristiquesRepository.save(caracteristiques);
            return updatedCaracteristiques.getDto();
        } else {
            throw new EntityNotFoundException("Caracteristiques avec id " + id + " introuvable.");
        }
    }

    @Override
    public CaracteristiquesRequest getCacteristiquesByVahiculeId (Integer vehiculeId){
        Caracteristiques caracteristiques = caracteristiquesRepository.findByVehiculeId(vehiculeId);
        return caracteristiques.getDto();
    }


}
