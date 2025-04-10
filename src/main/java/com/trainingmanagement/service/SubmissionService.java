package com.trainingmanagement.service;

import com.trainingmanagement.dto.SubmissionDto;
import com.trainingmanagement.dto.UserDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Assignment;
import com.trainingmanagement.model.Submission;
import com.trainingmanagement.model.User;
import com.trainingmanagement.repository.AssignmentRepository;
import com.trainingmanagement.repository.SubmissionRepository;
import com.trainingmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;
    
    @Autowired
    public SubmissionService(
            SubmissionRepository submissionRepository,
            AssignmentRepository assignmentRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService,
            NotificationService notificationService) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }
    
    public SubmissionDto getSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Submission", "id", id));
        return mapToDto(submission);
    }
    
    public List<SubmissionDto> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", assignmentId));
        
        return submissionRepository.findByAssignment(assignment).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<SubmissionDto> getSubmissionsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        
        return submissionRepository.findByStudent(student).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public SubmissionDto getSubmissionByAssignmentAndStudent(Long assignmentId, Long studentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", assignmentId));
        
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        
        Submission submission = submissionRepository.findByAssignmentAndStudent(assignment, student)
            .orElseThrow(() -> new ResourceNotFoundException("Submission for assignment " + assignmentId + " and student " + studentId));
        
        return mapToDto(submission);
    }
    
    public SubmissionDto submitAssignment(MultipartFile file, String content, Long assignmentId, Long studentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", assignmentId));
        
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        
        // Check if submission already exists
        Optional<Submission> existingSubmission = submissionRepository.findByAssignmentAndStudent(assignment, student);
        
        Submission submission;
        if (existingSubmission.isPresent()) {
            submission = existingSubmission.get();
            
            // Delete old file if it exists
            if (submission.getFilePath() != null) {
                fileStorageService.deleteFile(submission.getFilePath());
            }
        } else {
            submission = new Submission();
            submission.setAssignment(assignment);
            submission.setStudent(student);
        }
        
        submission.setContent(content);
        submission.setSubmittedAt(LocalDateTime.now());
        
        // Store file if provided
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            submission.setFilePath(fileName);
        }
        
        Submission savedSubmission = submissionRepository.save(submission);
        
        // Notify instructor
        User instructor = assignment.getCourse().getInstructor();
        if (instructor != null) {
            notificationService.createSubmissionNotification(instructor, student, assignment.getTitle());
        }
        
        return mapToDto(savedSubmission);
    }
    
    public SubmissionDto gradeSubmission(Long id, SubmissionDto submissionDto) {
        Submission submission = submissionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Submission", "id", id));
        
        submission.setScore(submissionDto.getScore());
        submission.setFeedback(submissionDto.getFeedback());
        submission.setGradedAt(LocalDateTime.now());
        
        User gradedBy = userRepository.findById(submissionDto.getGradedBy().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", submissionDto.getGradedBy().getId()));
        
        submission.setGradedBy(gradedBy);
        
        Submission gradedSubmission = submissionRepository.save(submission);
        
        // Notify student
        notificationService.createGradingNotification(submission.getStudent(), submission.getAssignment().getTitle());
        
        return mapToDto(gradedSubmission);
    }
    
    public void deleteSubmission(Long id) {
        Submission submission = submissionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Submission", "id", id));
        
        // Delete file if exists
        if (submission.getFilePath() != null) {
            fileStorageService.deleteFile(submission.getFilePath());
        }
        
        submissionRepository.delete(submission);
    }
    
    private SubmissionDto mapToDto(Submission submission) {
        SubmissionDto dto = new SubmissionDto();
        dto.setId(submission.getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        
        UserDto studentDto = new UserDto();
        studentDto.setId(submission.getStudent().getId());
        studentDto.setEmail(submission.getStudent().getEmail());
        studentDto.setFirstName(submission.getStudent().getFirstName());
        studentDto.setLastName(submission.getStudent().getLastName());
        studentDto.setProfileImage(submission.getStudent().getProfileImage());
        studentDto.setRole(submission.getStudent().getRole());
        
        dto.setStudent(studentDto);
        dto.setContent(submission.getContent());
        dto.setFilePath(submission.getFilePath());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());
        dto.setGradedAt(submission.getGradedAt());
        
        if (submission.getGradedBy() != null) {
            UserDto gradedByDto = new UserDto();
            gradedByDto.setId(submission.getGradedBy().getId());
            gradedByDto.setEmail(submission.getGradedBy().getEmail());
            gradedByDto.setFirstName(submission.getGradedBy().getFirstName());
            gradedByDto.setLastName(submission.getGradedBy().getLastName());
            gradedByDto.setProfileImage(submission.getGradedBy().getProfileImage());
            gradedByDto.setRole(submission.getGradedBy().getRole());
            
            dto.setGradedBy(gradedByDto);
        }
        
        return dto;
    }
}

