package com.trainingmanagement.service;

import com.trainingmanagement.dto.AssignmentDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Assignment;
import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.User;
import com.trainingmanagement.repository.AssignmentRepository;
import com.trainingmanagement.repository.CourseRepository;
import com.trainingmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
    
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    @Autowired
    public AssignmentService(
            AssignmentRepository assignmentRepository,
            CourseRepository courseRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    
    public AssignmentDto getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", id));
        return mapToDto(assignment);
    }
    
    public List<AssignmentDto> getAssignmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        return assignmentRepository.findByCourse(course).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<AssignmentDto> getUpcomingAssignments() {
        LocalDateTime now = LocalDateTime.now();
        return assignmentRepository.findByDueDateAfter(now).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<AssignmentDto> getPastAssignments() {
        LocalDateTime now = LocalDateTime.now();
        return assignmentRepository.findByDueDateBefore(now).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public AssignmentDto createAssignment(AssignmentDto assignmentDto) {
        Course course = courseRepository.findById(assignmentDto.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", assignmentDto.getCourseId()));
        
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setDueDate(assignmentDto.getDueDate());
        assignment.setMaxScore(assignmentDto.getMaxScore());
        assignment.setCourse(course);
        
        Assignment savedAssignment = assignmentRepository.save(assignment);
        
        // Notify all students enrolled in the course
        course.getStudents().forEach(student -> 
            notificationService.createAssignmentNotification(student, course, savedAssignment.getTitle())
        );
        
        return mapToDto(savedAssignment);
    }
    
    public AssignmentDto updateAssignment(Long id, AssignmentDto assignmentDto) {
        Assignment assignment = assignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", id));
        
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setDueDate(assignmentDto.getDueDate());
        assignment.setMaxScore(assignmentDto.getMaxScore());
        
        if (assignmentDto.getCourseId() != null && !assignmentDto.getCourseId().equals(assignment.getCourse().getId())) {
            Course course = courseRepository.findById(assignmentDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", assignmentDto.getCourseId()));
            assignment.setCourse(course);
        }
        
        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return mapToDto(updatedAssignment);
    }
    
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment", "id", id);
        }
        assignmentRepository.deleteById(id);
    }
    
    private AssignmentDto mapToDto(Assignment assignment) {
        AssignmentDto dto = new AssignmentDto();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxScore(assignment.getMaxScore());
        dto.setCourseId(assignment.getCourse().getId());
        dto.setSubmissionCount(assignment.getSubmissions().size());
        return dto;
    }
}

