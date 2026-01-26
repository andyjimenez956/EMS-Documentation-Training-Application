package edu.wgu.d.emsbackend.scenario;

import edu.wgu.d.emsbackend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "scenarios")
public class Scenario extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String prompt;

    @Column(nullable = false)
    private boolean active = true;

    public Scenario() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
