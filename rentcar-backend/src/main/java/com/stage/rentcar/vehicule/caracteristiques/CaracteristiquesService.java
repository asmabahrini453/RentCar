package com.stage.rentcar.vehicule.caracteristiques;

import java.util.List;

public interface CaracteristiquesService {
    CaracteristiquesRequest createCaracteristique(CaracteristiquesRequest request);
    List<CaracteristiquesRequest> getAllCaracteristiques();
    CaracteristiquesRequest updateCaracteristiques (Integer id , CaracteristiquesRequest request);
     CaracteristiquesRequest getCacteristiquesByVahiculeId (Integer vehiculeId);
}
