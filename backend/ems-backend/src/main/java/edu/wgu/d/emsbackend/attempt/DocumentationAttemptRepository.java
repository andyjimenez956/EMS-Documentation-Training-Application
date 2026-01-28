package edu.wgu.d.emsbackend.attempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocumentationAttemptRepository extends JpaRepository<DocumentationAttempt, UUID> {

    @Query("""
        select a from DocumentationAttempt a
        where a.studentId = :studentId
        order by
          case when a.status = edu.wgu.d.emsbackend.attempt.AttemptStatus.SUBMITTED then 0 else 1 end,
          a.submittedAt desc,
          a.created desc
    """)
    List<DocumentationAttempt> findByStudentOrdered(UUID studentId);

    @Query("""
        select a from DocumentationAttempt a
        where a.scenarioId = :scenarioId
        order by
          case when a.status = edu.wgu.d.emsbackend.attempt.AttemptStatus.SUBMITTED then 0 else 1 end,
          a.submittedAt desc,
          a.created desc
    """)
    List<DocumentationAttempt> findByScenarioOrdered(UUID scenarioId);
}
