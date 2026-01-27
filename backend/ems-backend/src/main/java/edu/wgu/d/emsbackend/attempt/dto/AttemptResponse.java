package edu.wgu.d.emsbackend.attempt.dto;

import edu.wgu.d.emsbackend.attempt.AttemptStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttemptResponse(
        UUID id,
        UUID scenarioId,
        UUID studentId,
        AttemptStatus status,
        LocalDateTime submittedAt,
        String patientFirstName,
        String patientLastName,
        Integer patientAge,
        String patientSex,
        String chiefComplaint,
        Integer systolicBp,
        Integer diastolicBp,
        Integer heartRate,
        Integer respiratoryRate,
        Integer spo2,
        String assessmentNotes,
        String interventions,
        String disposition,
        String narrativeText,
        Integer score,
        String feedback
) {}
