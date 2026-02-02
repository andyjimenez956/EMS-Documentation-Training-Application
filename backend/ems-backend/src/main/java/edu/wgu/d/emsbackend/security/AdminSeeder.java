package edu.wgu.d.emsbackend.security;

import edu.wgu.d.emsbackend.user.Role;
import edu.wgu.d.emsbackend.user.User;
import edu.wgu.d.emsbackend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminSeeder(UserRepository users) {
        this.users = users;
    }

    @Override
    public void run(String... args) {
        String email = "admin1@ems.local";

        if (users.findByEmailIgnoreCase(email).isPresent()) return;

        User admin = new User();
        admin.setEmail(email);
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(Role.ADMIN);
        admin.setPasswordHash(encoder.encode("Admin123!"));

        users.save(admin);

        System.out.println("✅ Seeded admin user: " + email);
    }
}
