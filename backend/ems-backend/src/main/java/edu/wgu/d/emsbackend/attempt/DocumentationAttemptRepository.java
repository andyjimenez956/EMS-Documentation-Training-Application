package edu.wgu.d.emsbackend.attempt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentationAttemptRepository extends JpaRepository<DocumentationAttempt, UUID> {
    List<DocumentationAttempt> findByStudentIdOrderBySubmittedAtDesc(UUID studentId);
    List<DocumentationAttempt> findByScenarioIdOrderBySubmittedAtDesc(UUID scenarioId);
}
