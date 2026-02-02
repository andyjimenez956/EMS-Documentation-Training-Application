package edu.wgu.d.emsbackend.attempt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentationAttemptRepository extends JpaRepository<DocumentationAttempt, UUID> {

    List<DocumentationAttempt> findByStudentIdOrderByCreatedDesc(UUID studentId);

    List<DocumentationAttempt> findByStatusOrderBySubmittedAtDesc(AttemptStatus status);

    Optional<DocumentationAttempt> findByIdAndStudentId(UUID id, UUID studentId);
}
