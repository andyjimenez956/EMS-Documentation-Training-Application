package edu.wgu.d.emsbackend.attempt;

import edu.wgu.d.emsbackend.attempt.dto.AttemptResponse;
import edu.wgu.d.emsbackend.attempt.dto.CreateAttemptRequest;
import edu.wgu.d.emsbackend.attempt.dto.UpdateAttemptRequest;
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
    public AttemptResponse createDraft(@Valid @RequestBody CreateAttemptRequest req) {
        return toResponse(attemptService.createDraft(req));
    }

    @PutMapping("/{id}")
    public AttemptResponse updateDraft(@PathVariable UUID id, @RequestBody UpdateAttemptRequest req) {
        return toResponse(attemptService.updateDraft(id, req));
    }

    @DeleteMapping("/{id}")
    public void deleteDraft(@PathVariable UUID id) {
        attemptService.deleteDraft(id);
    }

    @PostMapping("/{id}/submit")
    public AttemptResponse submit(@PathVariable UUID id) {
        return toResponse(attemptService.submit(id));
    }

    @GetMapping
    public List<AttemptResponse> listAll(@RequestParam(required = false) AttemptStatus status) {
        if (status != null) {
            return attemptService.listByStatus(status).stream().map(this::toResponse).toList();
        }
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
                a.getStatus(),
                a.getSubmittedAt(),
                a.getPatientFirstName(),
                a.getPatientLastName(),
                a.getPatientAge(),
                a.getPatientSex(),
                a.getChiefComplaint(),
                a.getSystolicBp(),
                a.getDiastolicBp(),
                a.getHeartRate(),
                a.getRespiratoryRate(),
                a.getSpo2(),
                a.getAssessmentNotes(),
                a.getInterventions(),
                a.getDisposition(),
                a.getNarrativeText(),
                a.getScore(),
                a.getFeedback()
        );
    }
}
