package edu.wgu.d.emsbackend.report;

import edu.wgu.d.emsbackend.user.User;
import edu.wgu.d.emsbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final UserRepository userRepository;

    public ReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ReportResponse generateUserReport() {

        String title = "EMS Documentation Training Application - User Report";
        LocalDateTime generatedAt = LocalDateTime.now();

        List<String> columns = List.of(
                "ID",
                "Email",
                "First Name",
                "Last Name",
                "Role",
                "Created"
        );

        List<User> users = userRepository.findAll();

        List<List<String>> rows = users.stream()
                .map(u -> List.<String>of(
                        String.valueOf(u.getId()),
                        String.valueOf(u.getEmail()),
                        String.valueOf(u.getFirstName()),
                        String.valueOf(u.getLastName()),
                        String.valueOf(u.getRole()),
                        String.valueOf(u.getCreated())   // or getCreatedAt() depending on BaseEntity
                ))
                .toList();


        return new ReportResponse(title, generatedAt, columns, rows);
    }
}
