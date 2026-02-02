package edu.wgu.d.emsbackend.attempt;

import edu.wgu.d.emsbackend.attempt.dto.AttemptResponse;
import edu.wgu.d.emsbackend.attempt.dto.CreateAttemptRequest;
import edu.wgu.d.emsbackend.attempt.dto.ReviewAttemptRequest;
import edu.wgu.d.emsbackend.attempt.dto.UpdateAttemptRequest;
import edu.wgu.d.emsbackend.attempt.service.DocumentationAttemptService;
import edu.wgu.d.emsbackend.security.DbUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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
    public AttemptResponse createDraft(Authentication auth, @Valid @RequestBody CreateAttemptRequest req) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return toResponse(attemptService.createDraft(actor, req));
    }

    @PutMapping("/{id}")
    public AttemptResponse updateDraft(Authentication auth, @PathVariable UUID id, @Valid @RequestBody UpdateAttemptRequest req) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return toResponse(attemptService.updateDraft(actor, id, req));
    }

    @DeleteMapping("/{id}")
    public void deleteDraft(Authentication auth, @PathVariable UUID id) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        attemptService.deleteDraft(actor, id);
    }

    @PostMapping("/{id}/submit")
    public AttemptResponse submit(Authentication auth, @PathVariable UUID id) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return toResponse(attemptService.submit(actor, id));
    }

    @PostMapping("/{id}/review")
    public AttemptResponse review(Authentication auth, @PathVariable UUID id, @Valid @RequestBody ReviewAttemptRequest req) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return toResponse(attemptService.reviewSubmitted(actor, id, req));
    }

    @GetMapping("/{id}")
    public AttemptResponse getOne(Authentication auth, @PathVariable UUID id) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return toResponse(attemptService.getOne(actor, id));
    }

    @GetMapping("/my")
    public List<AttemptResponse> my(Authentication auth) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return attemptService.listMy(actor).stream().map(this::toResponse).toList();
    }

    @GetMapping("/submitted")
    public List<AttemptResponse> submitted(Authentication auth) {
        DbUserPrincipal actor = (DbUserPrincipal) auth.getPrincipal();
        return attemptService.listSubmitted(actor).stream().map(this::toResponse).toList();
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
