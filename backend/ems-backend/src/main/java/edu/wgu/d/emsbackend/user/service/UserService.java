package edu.wgu.d.emsbackend.user.service;

import edu.wgu.d.emsbackend.user.User;
import edu.wgu.d.emsbackend.user.UserRepository;
import edu.wgu.d.emsbackend.user.dto.CreateUserRequest;
import edu.wgu.d.emsbackend.user.dto.UpdateUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());

        // Optional but recommended: avoid duplicate emails
        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + normalizedEmail);
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setRole(request.getRole());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public void deleteUser(UUID id) {
        // Optional: verify exists so delete gives a clear error
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Search logic used by /api/users/search:
     * - If email provided => exact match (case-insensitive)
     * - else if lastName provided => contains (case-insensitive)
     * - else => return all
     */
    public List<User> searchUsers(String email, String lastName) {
        boolean hasEmail = email != null && !email.isBlank();
        boolean hasLast = lastName != null && !lastName.isBlank();

        if (hasEmail) {
            String normalizedEmail = normalizeEmail(email);
            return userRepository.findByEmailIgnoreCase(normalizedEmail)
                    .map(List::of)
                    .orElseGet(List::of);
        }

        if (hasLast) {
            return userRepository.findByLastNameIgnoreCaseContaining(lastName.trim());
        }

        return userRepository.findAll();
    }

    public User updateUser(UUID id, UpdateUserRequest request) {
        User existing = getUser(id);

        String normalizedEmail = normalizeEmail(request.getEmail());

        // Optional but recommended: avoid taking someone else's email
        userRepository.findByEmailIgnoreCase(normalizedEmail).ifPresent(other -> {
            if (!other.getId().equals(existing.getId())) {
                throw new IllegalArgumentException("Email already exists: " + normalizedEmail);
            }
        });

        existing.setEmail(normalizedEmail);
        existing.setFirstName(request.getFirstName().trim());
        existing.setLastName(request.getLastName().trim());
        existing.setRole(request.getRole());

        return userRepository.save(existing);
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }
}
