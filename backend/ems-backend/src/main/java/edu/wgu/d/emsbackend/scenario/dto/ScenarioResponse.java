package edu.wgu.d.emsbackend.scenario.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScenarioResponse(
        UUID id,
        String title,
        String prompt,
        boolean active,
        LocalDateTime createdAt
) {}
