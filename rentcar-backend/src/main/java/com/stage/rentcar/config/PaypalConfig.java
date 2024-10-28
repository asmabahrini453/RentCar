package com.stage.rentcar.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {
    @Value("${application.paypal.client-id}")
    private String clientId ;
    @Value("${application.paypal.client-secret}")
    private String clientSecret ;
    @Value("${application.paypal.mode}")
    private String mode ;

    @Bean
    public APIContext apiContext (){
        return new APIContext(clientId,clientSecret,mode);
    }


}
