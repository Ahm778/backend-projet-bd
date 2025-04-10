package com.trainingmanagement.repository;

import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourseOrderByOrderIndex(Course course);
}

