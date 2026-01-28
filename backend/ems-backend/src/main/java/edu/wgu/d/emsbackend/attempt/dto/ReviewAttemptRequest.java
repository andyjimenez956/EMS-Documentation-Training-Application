package edu.wgu.d.emsbackend.attempt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.UUID;

public class ReviewAttemptRequest {

    @Min(0)
    @Max(100)
    private Integer score;

    private String feedback;

    private UUID reviewedBy;

    public Integer getScore() { return score; }
    public String getFeedback() { return feedback; }
    public UUID getReviewedBy() { return reviewedBy; }
}
