package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String adminEmail = "admin@email.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {

                User admin = new User(
                        "Administrador",
                        adminEmail,
                        passwordEncoder.encode("admin123"),
                        "ADMIN"
                );

                userRepository.save(admin);

                System.out.println("======================================");
                System.out.println("ADMIN inicial criado");
                System.out.println("Email: admin@email.com");
                System.out.println("Senha: admin123");
                System.out.println("======================================");
            }
        };
    }
}