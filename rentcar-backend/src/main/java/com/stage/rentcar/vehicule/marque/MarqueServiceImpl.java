package com.stage.rentcar.vehicule.marque;

import com.stage.rentcar.User.User;
import com.stage.rentcar.file.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MarqueServiceImpl implements MarqueService {

    private final MarqueRepository marqueRepository;
    private final FileStorageService fileStorageService;

    @Override
    public MarqueRequest createMarque(MarqueRequest request) {
        Marque marque = new Marque();
        marque.setNom(request.getNom());
        marqueRepository.save(marque);
        return marque.getDto();
    }

    @Override
    public List<MarqueRequest> getAllMarque() {
        List<Marque> requests = marqueRepository.findAll();
        return requests.stream().map(Marque::getDto).collect(Collectors.toList());
    }

    @Override
    public MarqueRequest updateMarque(Integer id, MarqueRequest request) {
        Optional<Marque> optionalMarque = marqueRepository.findById(id);
        if (optionalMarque.isPresent()) {
            Marque marque = optionalMarque.get();

            if (request.getNom() != null) {
                marque.setNom(request.getNom());
            }

            Marque updatedMarque = marqueRepository.save(marque);
            return updatedMarque.getDto();
        } else {
            throw new EntityNotFoundException("marque avec id " + id + " introuvable.");
        }
    }


}
