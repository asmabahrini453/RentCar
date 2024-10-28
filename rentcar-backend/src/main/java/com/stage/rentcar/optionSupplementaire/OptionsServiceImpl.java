package com.stage.rentcar.optionSupplementaire;

import com.stage.rentcar.agence.Agence;
import com.stage.rentcar.agence.AgenceRepository;
import com.stage.rentcar.agence.AgenceRequest;
import com.stage.rentcar.handler.ResourceNotFoundException;
import com.stage.rentcar.vehicule.Vehicule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionsServiceImpl implements OptionsService{
    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private AgenceRepository agenceRepository;

    @Override
    public OptionsRequestChef createOption(OptionsRequestChef optionsRequest) {
        Agence agence = agenceRepository.findById(optionsRequest.getAgenceId())
                .orElseThrow(() -> new IllegalArgumentException("Agence not found"));

        Options options = Options.builder()
                .descSiegeEnfant(optionsRequest.getDescSiegeEnfant())
                .prixSiegeEnfant(optionsRequest.getPrixSiegeEnfant())
                .descSiegeBebe(optionsRequest.getDescSiegeBebe())
                .prixSiegeBebe(optionsRequest.getPrixSiegeBebe())
                .descGPS(optionsRequest.getDescGPS())
                .prixGPS(optionsRequest.getPrixGPS())
                .descAssistanceRoutiere(optionsRequest.getDescAssistanceRoutiere())
                .prixAssistanceRoutiere(optionsRequest.getPrixAssistanceRoutiere())
                .descProtectionComplete(optionsRequest.getDescProtectionComplete())
                .prixProtectionComplete(optionsRequest.getPrixProtectionComplete())
                .agence(agence)
                .build();
     optionsRepository.save(options);
        return options.getDtoChef();
    }

    @Override
    public List<OptionsRequestChef> getAllOptions() {
        List<Options> options= optionsRepository.findAll();
        return options.stream().map(Options::getDtoChef).collect(Collectors.toList());
    }


    @Override
    public OptionsRequestChef updateOption(Integer id, OptionsRequestChef optionsRequest) {
        Optional<Options> optionalOptions = optionsRepository.findById(id);
        if (optionalOptions.isPresent()) {
            Options options = optionalOptions.get();
            options.setDescSiegeEnfant(optionsRequest.getDescSiegeEnfant());
            options.setPrixSiegeEnfant(optionsRequest.getPrixSiegeEnfant());
            options.setDescSiegeBebe(optionsRequest.getDescSiegeBebe());
            options.setPrixSiegeBebe(optionsRequest.getPrixSiegeBebe());
            options.setDescGPS(optionsRequest.getDescGPS());
            options.setPrixGPS(optionsRequest.getPrixGPS());
            options.setDescAssistanceRoutiere(optionsRequest.getDescAssistanceRoutiere());
            options.setPrixAssistanceRoutiere(optionsRequest.getPrixAssistanceRoutiere());
            options.setDescProtectionComplete(optionsRequest.getDescProtectionComplete());
            options.setPrixProtectionComplete(optionsRequest.getPrixProtectionComplete());
            Agence agence = agenceRepository.findById(optionsRequest.getAgenceId()).orElseThrow();
            options.setAgence(agence);
            optionsRepository.save(options);
            return options.getDtoChef();
        }
        else {
            throw new EntityNotFoundException(" not found with ID: " + id);
        }

    }

    @Override
    public OptionsRequestChef getOptionsByAgenceId(Integer agenceId) {
        Options options = optionsRepository.findByAgenceId(agenceId);

        if (options == null) {
            log.error("No options found for agenceId: " + agenceId);
            throw new ResourceNotFoundException("Options not found for agenceId: " + agenceId);
        }

        return options.getDtoChef();
    }

    @Override
    public OptionPricesRequest getOptionsPricesByAgenceId(Integer agenceId) {
        Options options = optionsRepository.findByAgenceId(agenceId);
        return options.getDtoPrices();
    }
}
