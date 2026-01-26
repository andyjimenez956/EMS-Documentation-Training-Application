package edu.wgu.d.emsbackend.attempt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateAttemptRequest {
    @NotNull private UUID scenarioId;
    @NotNull private UUID studentId;
    @NotBlank private String narrativeText;

    public UUID getScenarioId() { return scenarioId; }
    public UUID getStudentId() { return studentId; }
    public String getNarrativeText() { return narrativeText; }
}
