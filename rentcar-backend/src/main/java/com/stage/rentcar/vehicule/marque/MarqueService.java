package com.stage.rentcar.vehicule.marque;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MarqueService {
    MarqueRequest createMarque(MarqueRequest request) ;
    List<MarqueRequest> getAllMarque() ;

    MarqueRequest updateMarque(Integer id, MarqueRequest request);

}
