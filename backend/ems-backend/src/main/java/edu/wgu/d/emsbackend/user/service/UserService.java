package edu.wgu.d.emsbackend.user.service;

import edu.wgu.d.emsbackend.user.User;
import edu.wgu.d.emsbackend.user.UserRepository;
import edu.wgu.d.emsbackend.user.dto.CreateUserRequest;
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
        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
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
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + id)
        );
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public List<User> searchByLastName(String lastNamePart) {
        return userRepository.findByLastNameIgnoreCaseContaining(lastNamePart.trim());
    }

    public User updateUser(UUID id, edu.wgu.d.emsbackend.user.dto.UpdateUserRequest request) {

        User existing = getUser(id);

        existing.setEmail(request.getEmail().trim().toLowerCase());
        existing.setFirstName(request.getFirstName().trim());
        existing.setLastName(request.getLastName().trim());
        existing.setRole(request.getRole());

        return userRepository.save(existing);
    }

}

