package com.trainingmanagement.repository;

import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.Document;
import com.trainingmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCourse(Course course);
    List<Document> findByUploader(User uploader);
}

