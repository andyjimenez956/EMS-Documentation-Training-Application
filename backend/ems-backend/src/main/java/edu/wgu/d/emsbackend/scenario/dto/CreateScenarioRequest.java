package edu.wgu.d.emsbackend.scenario.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateScenarioRequest {
    @NotBlank private String title;
    @NotBlank private String prompt;
    private Boolean active;

    public String getTitle() { return title; }
    public String getPrompt() { return prompt; }
    public Boolean getActive() { return active; }
}
