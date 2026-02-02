package edu.wgu.d.emsbackend.attempt.service;

import edu.wgu.d.emsbackend.attempt.AttemptStatus;
import edu.wgu.d.emsbackend.attempt.DocumentationAttempt;
import edu.wgu.d.emsbackend.attempt.DocumentationAttemptRepository;
import edu.wgu.d.emsbackend.attempt.dto.CreateAttemptRequest;
import edu.wgu.d.emsbackend.attempt.dto.ReviewAttemptRequest;
import edu.wgu.d.emsbackend.attempt.dto.UpdateAttemptRequest;
import edu.wgu.d.emsbackend.security.DbUserPrincipal;
import edu.wgu.d.emsbackend.user.Role;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentationAttemptService {

    private final DocumentationAttemptRepository attemptRepository;

    public DocumentationAttemptService(DocumentationAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public DocumentationAttempt createDraft(DbUserPrincipal actor, CreateAttemptRequest req) {
        if (actor.getRole() != Role.STUDENT) {
            throw new IllegalStateException("Only students can create reports");
        }
        if (!actor.getId().equals(req.getStudentId())) {
            throw new IllegalStateException("StudentId must match logged in user");
        }

        DocumentationAttempt a = new DocumentationAttempt();
        a.setScenarioId(req.getScenarioId());
        a.setStudentId(req.getStudentId());
        a.setStatus(AttemptStatus.DRAFT);

        applyCreateFields(a, req);

        return attemptRepository.save(a);
    }

    public DocumentationAttempt updateDraft(DbUserPrincipal actor, UUID id, UpdateAttemptRequest req) {
        DocumentationAttempt a = getForStudentOrInstructor(actor, id);

        if (actor.getRole() == Role.STUDENT && a.getStatus() == AttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Report is submitted and locked: " + id);
        }

        applyUpdateFields(a, req);

        return attemptRepository.save(a);
    }

    public void deleteDraft(DbUserPrincipal actor, UUID id) {
        DocumentationAttempt a = getForStudentOrInstructor(actor, id);

        if (actor.getRole() == Role.STUDENT && a.getStatus() == AttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Report is submitted and locked: " + id);
        }

        attemptRepository.delete(a);
    }

    public DocumentationAttempt submit(DbUserPrincipal actor, UUID id) {
        DocumentationAttempt a = getForStudent(actor, id);

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

    public DocumentationAttempt reviewSubmitted(DbUserPrincipal actor, UUID id, ReviewAttemptRequest req) {
        if (actor.getRole() != Role.INSTRUCTOR) {
            throw new IllegalStateException("Only instructors can review reports");
        }

        DocumentationAttempt a = getForInstructor(id);

        if (a.getStatus() != AttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Only submitted reports can be reviewed");
        }

        a.setScore(req.getScore());
        a.setFeedback(isBlank(req.getFeedback()) ? null : req.getFeedback().trim());
        a.setReviewedBy(actor.getId());
        a.setReviewedAt(LocalDateTime.now());

        return attemptRepository.save(a);
    }

    public DocumentationAttempt getOne(DbUserPrincipal actor, UUID id) {
        return getForStudentOrInstructor(actor, id);
    }

    public List<DocumentationAttempt> listMy(DbUserPrincipal actor) {
        if (actor.getRole() != Role.STUDENT) {
            throw new IllegalStateException("Only students can use /my");
        }
        return attemptRepository.findByStudentIdOrderByCreatedDesc(actor.getId());
    }

    public List<DocumentationAttempt> listSubmitted(DbUserPrincipal actor) {
        if (actor.getRole() != Role.INSTRUCTOR) {
            throw new IllegalStateException("Only instructors can view submitted list");
        }
        return attemptRepository.findByStatusOrderBySubmittedAtDesc(AttemptStatus.SUBMITTED);
    }

    private DocumentationAttempt getForStudent(DbUserPrincipal actor, UUID id) {
        if (actor.getRole() != Role.STUDENT) {
            throw new IllegalStateException("Only students can perform this action");
        }
        return attemptRepository.findByIdAndStudentId(id, actor.getId())
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found: " + id));
    }

    private DocumentationAttempt getForInstructor(UUID id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found: " + id));
    }

    private DocumentationAttempt getForStudentOrInstructor(DbUserPrincipal actor, UUID id) {
        if (actor.getRole() == Role.INSTRUCTOR) {
            return getForInstructor(id);
        }
        return getForStudent(actor, id);
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
        String name = (nullSafeS(a.getPatientFirstName()) + " " + nullSafeS(a.getPatientLastName())).trim();
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

    private String nullSafeS(String s) {
        return s == null ? "" : s;
    }
}
