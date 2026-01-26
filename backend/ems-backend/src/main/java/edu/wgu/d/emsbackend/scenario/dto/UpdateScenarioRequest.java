package edu.wgu.d.emsbackend.scenario.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateScenarioRequest {
    @NotBlank private String title;
    @NotBlank private String prompt;
    private boolean active;

    public String getTitle() { return title; }
    public String getPrompt() { return prompt; }
    public boolean isActive() { return active; }
}
