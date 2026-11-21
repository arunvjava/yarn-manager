package com.tiruppur.yarnmanager.security;

import com.tiruppur.yarnmanager.domain.auth.entity.Role;
import com.tiruppur.yarnmanager.domain.auth.entity.User;
import com.tiruppur.yarnmanager.domain.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            logger.info("Initializing default plant personnel user accounts for Tiruppur Knitwear ERP...");

            List<User> defaultUsers = List.of(
                User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Ramasamy (Managing Director)")
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .build(),

                User.builder()
                    .username("supervisor")
                    .password(passwordEncoder.encode("super123"))
                    .fullName("Muruganandam (Knitting & Sewing Head)")
                    .role(Role.ROLE_KNITTING_SUPERVISOR)
                    .active(true)
                    .build(),

                User.builder()
                    .username("qc_lead")
                    .password(passwordEncoder.encode("qc123"))
                    .fullName("Senthil Kumar (Quality Head)")
                    .role(Role.ROLE_QC_INSPECTOR)
                    .active(true)
                    .build(),

                User.builder()
                    .username("yarn_mgr")
                    .password(passwordEncoder.encode("yarn123"))
                    .fullName("Palanisamy (Yarn Procurement Head)")
                    .role(Role.ROLE_YARN_MANAGER)
                    .active(true)
                    .build(),

                User.builder()
                    .username("dispatch_officer")
                    .password(passwordEncoder.encode("dispatch123"))
                    .fullName("Karthik (Logistics & Shipping Officer)")
                    .role(Role.ROLE_DISPATCH_OFFICER)
                    .active(true)
                    .build()
            );

            userRepository.saveAll(defaultUsers);
            logger.info("Successfully provisioned {} factory user accounts.", defaultUsers.size());
        }
    }
}
