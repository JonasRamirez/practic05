package com.spring.practica1;

import com.spring.practica1.domain.model.Role;
import com.spring.practica1.domain.model.User;
import com.spring.practica1.domain.repository.RoleRepository;
import com.spring.practica1.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class Practica5Application {

	public static void main(String[] args) {
		SpringApplication.run(Practica5Application.class, args);
	}

	@Bean
	CommandLineRunner initData(
			UserRepository userRepo,
			RoleRepository roleRepo,
			PasswordEncoder encoder,
			@Value("${APP_ADMIN_USERNAME:admin}") String adminUsername,
			@Value("${APP_ADMIN_PASSWORD:se}") String adminPassword
	) {
		return args -> {

			Role adminRole = roleRepo.findByName("ADMIN")
					.orElseGet(() -> roleRepo.save(new Role("ADMIN")));

			Role userRole = roleRepo.findByName("USER")
					.orElseGet(() -> roleRepo.save(new Role("USER")));

			if (userRepo.findByUsername(adminUsername).isEmpty()) {
				User admin = new User();
				admin.setUsername(adminUsername);
				admin.setPassword(encoder.encode(adminPassword));
				admin.setRoles(Set.of(adminRole));
				userRepo.save(admin);
			}
		};
	}

}
