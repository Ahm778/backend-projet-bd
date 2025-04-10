package com.trainingmanagement.controller;

import com.trainingmanagement.dto.AssignmentDto;
import com.trainingmanagement.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    
    private final AssignmentService assignmentService;
    
    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDto> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssignmentDto>> getAssignmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByCourse(courseId));
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<AssignmentDto>> getUpcomingAssignments() {
        return ResponseEntity.ok(assignmentService.getUpcomingAssignments());
    }
    
    @GetMapping("/past")
    public ResponseEntity<List<AssignmentDto>> getPastAssignments() {
        return ResponseEntity.ok(assignmentService.getPastAssignments());
    }
    
    @PostMapping
    public ResponseEntity<AssignmentDto> createAssignment(@RequestBody AssignmentDto assignmentDto) {
        return ResponseEntity.ok(assignmentService.createAssignment(assignmentDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDto> updateAssignment(@PathVariable Long id, @RequestBody AssignmentDto assignmentDto) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, assignmentDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}

