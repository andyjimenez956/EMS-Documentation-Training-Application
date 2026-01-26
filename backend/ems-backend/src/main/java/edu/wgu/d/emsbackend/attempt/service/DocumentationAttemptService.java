package edu.wgu.d.emsbackend.attempt.service;

import edu.wgu.d.emsbackend.attempt.DocumentationAttempt;
import edu.wgu.d.emsbackend.attempt.DocumentationAttemptRepository;
import edu.wgu.d.emsbackend.attempt.dto.CreateAttemptRequest;
import edu.wgu.d.emsbackend.scenario.service.ScenarioService;
import edu.wgu.d.emsbackend.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentationAttemptService {

    private final DocumentationAttemptRepository attemptRepository;
    private final ScenarioService scenarioService;
    private final UserService userService;

    public DocumentationAttemptService(
            DocumentationAttemptRepository attemptRepository,
            ScenarioService scenarioService,
            UserService userService
    ) {
        this.attemptRepository = attemptRepository;
        this.scenarioService = scenarioService;
        this.userService = userService;
    }

    public DocumentationAttempt submit(CreateAttemptRequest req) {
        // Validate IDs exist (rubric-friendly business validation)
        scenarioService.get(req.getScenarioId());
        userService.getUser(req.getStudentId());

        DocumentationAttempt a = new DocumentationAttempt();
        a.setScenarioId(req.getScenarioId());
        a.setStudentId(req.getStudentId());
        a.setNarrativeText(req.getNarrativeText().trim());
        return attemptRepository.save(a);
    }

    public DocumentationAttempt get(UUID id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found: " + id));
    }

    public List<DocumentationAttempt> listAll() {
        return attemptRepository.findAll();
    }

    public List<DocumentationAttempt> listByStudent(UUID studentId) {
        return attemptRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);
    }

    public List<DocumentationAttempt> listByScenario(UUID scenarioId) {
        return attemptRepository.findByScenarioIdOrderBySubmittedAtDesc(scenarioId);
    }
}
