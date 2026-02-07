package edu.wgu.d.emsbackend.security;

import edu.wgu.d.emsbackend.user.Role;
import edu.wgu.d.emsbackend.user.User;
import edu.wgu.d.emsbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for "login behavior" in a Basic Auth system.
 * Spring Security calls DbUserDetailsService.loadUserByUsername(email).
 *
 * These tests validate:
 * 1) Missing user -> throws UsernameNotFoundException
 * 2) Existing user -> returns UserDetails with correct username + role authority
 */
@ExtendWith(MockitoExtension.class)
class DbUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DbUserDetailsService dbUserDetailsService;

    @Test
    void loadUserByUsername_whenUserDoesNotExist_throws() {
        String missingEmail = "missing@ems.local";

        when(userRepository.findByEmailIgnoreCase(missingEmail))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                dbUserDetailsService.loadUserByUsername(missingEmail)
        );
    }

    @Test
    void loadUserByUsername_whenUserExists_returnsUserDetailsWithRole() {
        String email = "student1@ems.local";

        User u = new User();
        u.setEmail(email);
        u.setFirstName("Student");
        u.setLastName("One");
        u.setRole(Role.STUDENT);
        u.setPasswordHash("anything"); // not used in this test

        when(userRepository.findByEmailIgnoreCase(email))
                .thenReturn(Optional.of(u));

        UserDetails details = dbUserDetailsService.loadUserByUsername(email);

        assertNotNull(details);
        assertEquals(email, details.getUsername());

        assertTrue(
                details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")),
                "Expected ROLE_STUDENT authority"
        );
    }
}
