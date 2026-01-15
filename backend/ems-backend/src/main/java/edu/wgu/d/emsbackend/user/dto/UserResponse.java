package edu.wgu.d.emsbackend.user.dto;

import edu.wgu.d.emsbackend.user.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private LocalDateTime created;

    public UserResponse(UUID id, String email, String firstName, String lastName, Role role, LocalDateTime created) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.created = created;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
