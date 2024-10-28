package com.stage.rentcar.optionSupplementaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OptionsService {
    OptionsRequestChef createOption(OptionsRequestChef optionsRequest) ;
    List<OptionsRequestChef> getAllOptions();
    OptionsRequestChef updateOption(Integer id ,OptionsRequestChef optionsRequest) ;
    OptionsRequestChef getOptionsByAgenceId(Integer agenceId) ;
    OptionPricesRequest getOptionsPricesByAgenceId(Integer agenceId);

}
