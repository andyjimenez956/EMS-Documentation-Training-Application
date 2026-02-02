package edu.wgu.d.emsbackend.report;

import edu.wgu.d.emsbackend.attempt.AttemptStatus;
import edu.wgu.d.emsbackend.attempt.DocumentationAttempt;
import edu.wgu.d.emsbackend.attempt.DocumentationAttemptRepository;
import edu.wgu.d.emsbackend.scenario.Scenario;
import edu.wgu.d.emsbackend.scenario.ScenarioRepository;
import edu.wgu.d.emsbackend.user.User;
import edu.wgu.d.emsbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final DocumentationAttemptRepository attemptRepository;
    private final ScenarioRepository scenarioRepository;

    public ReportService(
            UserRepository userRepository,
            DocumentationAttemptRepository attemptRepository,
            ScenarioRepository scenarioRepository
    ) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.scenarioRepository = scenarioRepository;
    }

    public ReportResponse usersReport() {
        List<User> users = userRepository.findAll();

        List<String> columns = List.of("id", "email", "firstName", "lastName", "role", "created");

        List<List<String>> rows = users.stream()
                .map(u -> List.of(
                        safe(u.getId()),
                        safe(u.getEmail()),
                        safe(u.getFirstName()),
                        safe(u.getLastName()),
                        safe(u.getRole()),
                        safe(u.getCreated())
                ))
                .toList();

        return new ReportResponse(
                "Users Report",
                LocalDateTime.now(),
                columns,
                rows
        );
    }

    public ReportResponse attemptsByScenarioReport() {
        List<DocumentationAttempt> attempts = attemptRepository.findAll();

        Map<UUID, String> scenarioTitles = scenarioRepository.findAll().stream()
                .collect(Collectors.toMap(Scenario::getId, s -> safe(s.getTitle())));

        Map<UUID, Long> submittedCounts = attempts.stream()
                .filter(a -> a.getStatus() == AttemptStatus.SUBMITTED)
                .collect(Collectors.groupingBy(DocumentationAttempt::getScenarioId, Collectors.counting()));

        List<Map.Entry<UUID, Long>> sorted = submittedCounts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .toList();

        List<String> columns = List.of("scenarioId", "scenarioTitle", "submittedCount");

        List<List<String>> rows = sorted.stream()
                .map(e -> List.of(
                        safe(e.getKey()),
                        scenarioTitles.getOrDefault(e.getKey(), ""),
                        String.valueOf(e.getValue())
                ))
                .toList();

        return new ReportResponse(
                "Submitted Attempts by Scenario",
                LocalDateTime.now(),
                columns,
                rows
        );
    }

    public ReportResponse attemptsByStatusReport() {
        List<DocumentationAttempt> attempts = attemptRepository.findAll();

        Map<AttemptStatus, Long> counts = attempts.stream()
                .collect(Collectors.groupingBy(DocumentationAttempt::getStatus, Collectors.counting()));

        List<AttemptStatus> order = List.of(AttemptStatus.DRAFT, AttemptStatus.SUBMITTED);

        List<String> columns = List.of("status", "count");

        List<List<String>> rows = order.stream()
                .map(s -> List.of(
                        s.name(),
                        String.valueOf(counts.getOrDefault(s, 0L))
                ))
                .toList();

        return new ReportResponse(
                "Attempts by Status",
                LocalDateTime.now(),
                columns,
                rows
        );
    }

    private String safe(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
