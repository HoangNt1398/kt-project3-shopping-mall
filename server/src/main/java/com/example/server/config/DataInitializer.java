package com.example.server.config;

import com.example.server.domain.admin.entity.Admin;
import com.example.server.domain.admin.repository.AdminRepository;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(AdminRepository adminRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.count() == 0) { // Check if there are no admins
                // Create and save user
                User user = new User();
                user.setLoginId("admin");
                user.setName("Admin Name");
                user.setPassword(passwordEncoder.encode("admin1234!")); // Sử dụng mật khẩu mã hóa
                user.setEmail("admin@example.com");
                user.setAddress("123 Admin Street");
                user.setRoles(List.of("ADMIN")); // Set roles as needed
                userRepository.save(user);

                // Create and save admin
                Admin admin = new Admin();
                admin.setLoginId("admin");
                admin.setPassword(passwordEncoder.encode("admin1234!")); // Use a strong password
                admin.setUser(user);
                adminRepository.save(admin);
            }
        };
    }
}
