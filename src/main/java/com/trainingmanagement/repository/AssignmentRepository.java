package com.trainingmanagement.repository;

import com.trainingmanagement.model.Assignment;
import com.trainingmanagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse(Course course);
    List<Assignment> findByDueDateBefore(LocalDateTime date);
    List<Assignment> findByDueDateAfter(LocalDateTime date);
}

