package edu.wgu.d.emsbackend.user;

import edu.wgu.d.emsbackend.user.dto.CreateUserRequest;
import edu.wgu.d.emsbackend.user.dto.UserResponse;
import edu.wgu.d.emsbackend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import edu.wgu.d.emsbackend.user.dto.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        User saved = userService.createUser(request);
        return toResponse(saved);
    }

    @GetMapping
    public List<UserResponse> listAll() {
        return userService.getAllUsers().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public UserResponse getOne(@PathVariable UUID id) {
        return toResponse(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String lastName) {
        return userService.searchByLastName(lastName).stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        User updated = userService.updateUser(id, request);
        return toResponse(updated);
    }


    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.getRole(),
                u.getCreated()
        );
    }
}
