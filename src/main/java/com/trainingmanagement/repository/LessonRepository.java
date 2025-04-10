package com.trainingmanagement.repository;

import com.trainingmanagement.model.Lesson;
import com.trainingmanagement.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleOrderByOrderIndex(Module module);
}

