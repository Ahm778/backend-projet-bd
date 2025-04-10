package com.trainingmanagement.repository;

import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
    List<Course> findByStudentsContaining(User student);
}

