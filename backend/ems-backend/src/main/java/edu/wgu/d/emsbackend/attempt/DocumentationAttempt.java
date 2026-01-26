package edu.wgu.d.emsbackend.attempt;

import edu.wgu.d.emsbackend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documentation_attempts")
public class DocumentationAttempt extends BaseEntity {

    @NotNull
    @Column(nullable = false)
    private UUID scenarioId;

    @NotNull
    @Column(nullable = false)
    private UUID studentId;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String narrativeText;

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    // Optional “review” fields (you can fill these later)
    private Integer score;
    @Column(columnDefinition = "text")
    private String feedback;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;

    public DocumentationAttempt() {}

    public UUID getScenarioId() { return scenarioId; }
    public void setScenarioId(UUID scenarioId) { this.scenarioId = scenarioId; }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public String getNarrativeText() { return narrativeText; }
    public void setNarrativeText(String narrativeText) { this.narrativeText = narrativeText; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public UUID getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(UUID reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
