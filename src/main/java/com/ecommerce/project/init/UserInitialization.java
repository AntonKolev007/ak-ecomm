package com.ecommerce.project.init;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class UserInitialization {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_SELLER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

            // Create users if not already present
            createOrUpdateUser(userRepository, "user1", "user1@example.com", "password1", userRoles, passwordEncoder);
            createOrUpdateUser(userRepository, "seller1", "seller1@example.com", "password2", sellerRoles, passwordEncoder);
            createOrUpdateUser(userRepository, "admin", "admin@example.com", "adminPass", adminRoles, passwordEncoder);
        };
    }

    private void createOrUpdateUser(UserRepository userRepository, String username, String email, String password, Set<Role> roles, PasswordEncoder passwordEncoder) {
        userRepository.findByUserName(username).ifPresentOrElse(user -> {
            user.setRoles(roles);
            userRepository.save(user);
        }, () -> {
            User newUser = new User(username, email, passwordEncoder.encode(password));
            newUser.setRoles(roles);
            userRepository.save(newUser);
        });
    }
}