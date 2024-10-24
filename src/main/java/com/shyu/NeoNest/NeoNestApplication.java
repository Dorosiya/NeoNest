package com.shyu.NeoNest;

import com.shyu.NeoNest.util.SpringSecurityAuditorAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NeoNestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeoNestApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}

}
