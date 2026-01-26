package edu.wgu.d.emsbackend.attempt;

import edu.wgu.d.emsbackend.attempt.dto.AttemptResponse;
import edu.wgu.d.emsbackend.attempt.dto.CreateAttemptRequest;
import edu.wgu.d.emsbackend.attempt.service.DocumentationAttemptService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attempts")
public class DocumentationAttemptController {

    private final DocumentationAttemptService attemptService;

    public DocumentationAttemptController(DocumentationAttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping
    public AttemptResponse submit(@Valid @RequestBody CreateAttemptRequest req) {
        return toResponse(attemptService.submit(req));
    }

    @GetMapping
    public List<AttemptResponse> listAll() {
        return attemptService.listAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public AttemptResponse getOne(@PathVariable UUID id) {
        return toResponse(attemptService.get(id));
    }

    @GetMapping("/by-student/{studentId}")
    public List<AttemptResponse> byStudent(@PathVariable UUID studentId) {
        return attemptService.listByStudent(studentId).stream().map(this::toResponse).toList();
    }

    @GetMapping("/by-scenario/{scenarioId}")
    public List<AttemptResponse> byScenario(@PathVariable UUID scenarioId) {
        return attemptService.listByScenario(scenarioId).stream().map(this::toResponse).toList();
    }

    private AttemptResponse toResponse(DocumentationAttempt a) {
        return new AttemptResponse(
                a.getId(),
                a.getScenarioId(),
                a.getStudentId(),
                a.getNarrativeText(),
                a.getSubmittedAt(),
                a.getScore(),
                a.getFeedback()
        );
    }
}
