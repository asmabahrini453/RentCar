package com.stage.rentcar;

import com.stage.rentcar.role.Role;
import com.stage.rentcar.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
@EnableScheduling
public class RentcarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentcarApplication.class, args);
	}


}
