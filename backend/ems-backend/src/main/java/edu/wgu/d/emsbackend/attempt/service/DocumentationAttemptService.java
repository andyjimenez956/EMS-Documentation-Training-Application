package edu.wgu.d.emsbackend.attempt.service;

import edu.wgu.d.emsbackend.attempt.AttemptStatus;
import edu.wgu.d.emsbackend.attempt.DocumentationAttempt;
import edu.wgu.d.emsbackend.attempt.DocumentationAttemptRepository;
import edu.wgu.d.emsbackend.attempt.dto.CreateAttemptRequest;
import edu.wgu.d.emsbackend.attempt.dto.UpdateAttemptRequest;
import edu.wgu.d.emsbackend.scenario.service.ScenarioService;
import edu.wgu.d.emsbackend.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public DocumentationAttempt createDraft(CreateAttemptRequest req) {
        scenarioService.get(req.getScenarioId());
        userService.getUser(req.getStudentId());

        DocumentationAttempt a = new DocumentationAttempt();
        a.setScenarioId(req.getScenarioId());
        a.setStudentId(req.getStudentId());
        a.setStatus(AttemptStatus.DRAFT);

        applyCreateFields(a, req);

        return attemptRepository.save(a);
    }

    public DocumentationAttempt updateDraft(UUID id, UpdateAttemptRequest req) {
        DocumentationAttempt a = get(id);

        if (a.getStatus() == AttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Report is submitted and locked: " + id);
        }

        applyUpdateFields(a, req);

        return attemptRepository.save(a);
    }

    public void deleteDraft(UUID id) {
        DocumentationAttempt a = get(id);

        if (a.getStatus() == AttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Report is submitted and locked: " + id);
        }

        attemptRepository.delete(a);
    }

    public DocumentationAttempt submit(UUID id) {
        DocumentationAttempt a = get(id);

        if (a.getStatus() == AttemptStatus.SUBMITTED) {
            return a;
        }

        validateForSubmit(a);

        if (isBlank(a.getNarrativeText())) {
            a.setNarrativeText(generateNarrative(a));
        }

        a.setStatus(AttemptStatus.SUBMITTED);
        a.setSubmittedAt(LocalDateTime.now());

        return attemptRepository.save(a);
    }

    public DocumentationAttempt get(UUID id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found: " + id));
    }

    public List<DocumentationAttempt> listAll() {
        return attemptRepository.findAll();
    }

    public List<DocumentationAttempt> listByStatus(AttemptStatus status) {
        return attemptRepository.findAll().stream()
                .filter(a -> a.getStatus() == status)
                .toList();
    }

    public List<DocumentationAttempt> listByStudent(UUID studentId) {
        return attemptRepository.findByStudentOrdered(studentId);
    }

    public List<DocumentationAttempt> listByScenario(UUID scenarioId) {
        return attemptRepository.findByScenarioOrdered(scenarioId);
    }




    private void applyCreateFields(DocumentationAttempt a, CreateAttemptRequest req) {
        if (!isBlank(req.getPatientFirstName())) a.setPatientFirstName(req.getPatientFirstName().trim());
        if (!isBlank(req.getPatientLastName())) a.setPatientLastName(req.getPatientLastName().trim());
        if (req.getPatientAge() != null) a.setPatientAge(req.getPatientAge());
        if (!isBlank(req.getPatientSex())) a.setPatientSex(req.getPatientSex().trim());
        if (!isBlank(req.getChiefComplaint())) a.setChiefComplaint(req.getChiefComplaint().trim());

        if (req.getSystolicBp() != null) a.setSystolicBp(req.getSystolicBp());
        if (req.getDiastolicBp() != null) a.setDiastolicBp(req.getDiastolicBp());
        if (req.getHeartRate() != null) a.setHeartRate(req.getHeartRate());
        if (req.getRespiratoryRate() != null) a.setRespiratoryRate(req.getRespiratoryRate());
        if (req.getSpo2() != null) a.setSpo2(req.getSpo2());

        if (!isBlank(req.getAssessmentNotes())) a.setAssessmentNotes(req.getAssessmentNotes().trim());
        if (!isBlank(req.getInterventions())) a.setInterventions(req.getInterventions().trim());
        if (!isBlank(req.getDisposition())) a.setDisposition(req.getDisposition().trim());
        if (!isBlank(req.getNarrativeText())) a.setNarrativeText(req.getNarrativeText().trim());
    }

    private void applyUpdateFields(DocumentationAttempt a, UpdateAttemptRequest req) {
        if (req.getPatientFirstName() != null) a.setPatientFirstName(trimOrEmpty(req.getPatientFirstName()));
        if (req.getPatientLastName() != null) a.setPatientLastName(trimOrEmpty(req.getPatientLastName()));
        if (req.getPatientAge() != null) a.setPatientAge(req.getPatientAge());
        if (req.getPatientSex() != null) a.setPatientSex(trimOrEmpty(req.getPatientSex()));
        if (req.getChiefComplaint() != null) a.setChiefComplaint(trimOrEmpty(req.getChiefComplaint()));

        if (req.getSystolicBp() != null) a.setSystolicBp(req.getSystolicBp());
        if (req.getDiastolicBp() != null) a.setDiastolicBp(req.getDiastolicBp());
        if (req.getHeartRate() != null) a.setHeartRate(req.getHeartRate());
        if (req.getRespiratoryRate() != null) a.setRespiratoryRate(req.getRespiratoryRate());
        if (req.getSpo2() != null) a.setSpo2(req.getSpo2());

        if (req.getAssessmentNotes() != null) a.setAssessmentNotes(trimOrNull(req.getAssessmentNotes()));
        if (req.getInterventions() != null) a.setInterventions(trimOrNull(req.getInterventions()));
        if (req.getDisposition() != null) a.setDisposition(trimOrEmpty(req.getDisposition()));
        if (req.getNarrativeText() != null) a.setNarrativeText(trimOrNull(req.getNarrativeText()));
    }

    private void validateForSubmit(DocumentationAttempt a) {
        if (isBlank(a.getChiefComplaint())) {
            throw new IllegalStateException("Chief complaint is required to submit");
        }
        if (a.getSystolicBp() == null || a.getDiastolicBp() == null) {
            throw new IllegalStateException("Blood pressure is required to submit");
        }
        if (a.getHeartRate() == null || a.getRespiratoryRate() == null) {
            throw new IllegalStateException("Heart rate and respiratory rate are required to submit");
        }
        if (isBlank(a.getDisposition())) {
            throw new IllegalStateException("Disposition is required to submit");
        }
    }

    private String generateNarrative(DocumentationAttempt a) {
        String name = (a.getPatientFirstName() + " " + a.getPatientLastName()).trim();
        if (isBlank(name)) name = "Patient";

        String ageSex = "";
        if (a.getPatientAge() != null && !isBlank(a.getPatientSex())) {
            ageSex = a.getPatientAge() + " y/o " + a.getPatientSex();
        } else if (a.getPatientAge() != null) {
            ageSex = a.getPatientAge() + " y/o";
        } else if (!isBlank(a.getPatientSex())) {
            ageSex = a.getPatientSex();
        }

        String vitals = "BP " + nullSafe(a.getSystolicBp()) + "/" + nullSafe(a.getDiastolicBp())
                + ", HR " + nullSafe(a.getHeartRate())
                + ", RR " + nullSafe(a.getRespiratoryRate());

        if (a.getSpo2() != null) {
            vitals += ", SpO2 " + a.getSpo2() + "%";
        }

        String narrative = name;
        if (!isBlank(ageSex)) narrative += " (" + ageSex + ")";
        narrative += " presents with " + a.getChiefComplaint().trim() + ". ";
        narrative += "Vitals: " + vitals + ". ";

        if (!isBlank(a.getAssessmentNotes())) {
            narrative += "Assessment: " + a.getAssessmentNotes().trim() + ". ";
        }

        if (!isBlank(a.getInterventions())) {
            narrative += "Interventions: " + a.getInterventions().trim() + ". ";
        }

        narrative += "Disposition: " + a.getDisposition().trim() + ".";

        return narrative.trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String trimOrEmpty(String s) {
        String t = s == null ? "" : s.trim();
        return t.isEmpty() ? "" : t;
    }

    private String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String nullSafe(Integer n) {
        return n == null ? "?" : String.valueOf(n);
    }
}
