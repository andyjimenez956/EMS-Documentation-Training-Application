package edu.wgu.d.emsbackend.attempt.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttemptResponse(
        UUID id,
        UUID scenarioId,
        UUID studentId,
        String narrativeText,
        LocalDateTime submittedAt,
        Integer score,
        String feedback
) {}
