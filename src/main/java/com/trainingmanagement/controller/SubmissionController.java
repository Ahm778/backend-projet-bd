package com.trainingmanagement.controller;

import com.trainingmanagement.dto.SubmissionDto;
import com.trainingmanagement.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    
    private final SubmissionService submissionService;
    
    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }
    
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignment(assignmentId));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId));
    }
    
    @GetMapping("/assignment/{assignmentId}/student/{studentId}")
    public ResponseEntity<SubmissionDto> getSubmissionByAssignmentAndStudent(
            @PathVariable Long assignmentId, @PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionByAssignmentAndStudent(assignmentId, studentId));
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubmissionDto> submitAssignment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("content") String content,
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok(submissionService.submitAssignment(file, content, assignmentId, studentId));
    }
    
    @PutMapping("/{id}/grade")
    public ResponseEntity<SubmissionDto> gradeSubmission(
            @PathVariable Long id,
            @RequestBody SubmissionDto submissionDto) {
        return ResponseEntity.ok(submissionService.gradeSubmission(id, submissionDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}

