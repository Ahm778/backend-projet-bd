package com.trainingmanagement.repository;

import com.trainingmanagement.model.Assignment;
import com.trainingmanagement.model.Submission;
import com.trainingmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignment(Assignment assignment);
    List<Submission> findByStudent(User student);
    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, User student);
    List<Submission> findByScoreIsNull();
}

