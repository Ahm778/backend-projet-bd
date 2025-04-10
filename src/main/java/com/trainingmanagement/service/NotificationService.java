package com.trainingmanagement.service;

import com.trainingmanagement.dto.NotificationDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.Notification;
import com.trainingmanagement.model.NotificationType;
import com.trainingmanagement.model.User;
import com.trainingmanagement.repository.NotificationRepository;
import com.trainingmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }
    
    public List<NotificationDto> getNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public Long countUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return notificationRepository.countByUserAndIsReadFalse(user);
    }
    
    public NotificationDto markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        
        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }
        
        return mapToDto(notification);
    }
    
    public void markAllAsRead(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        
        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(now);
        });
        
        notificationRepository.saveAll(unreadNotifications);
    }
    
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification", "id", id);
        }
        notificationRepository.deleteById(id);
    }
    
    // Helper methods to create different types of notifications
    
    public void createMessageNotification(User user, User sender, String subject) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("New Message");
        notification.setMessage("You have received a new message from " + sender.getFirstName() + " " + sender.getLastName() + ": " + subject);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(NotificationType.NEW_MESSAGE);
        notification.setLink("/messages");
        
        notificationRepository.save(notification);
    }
    
    public void createAssignmentNotification(User user, Course course, String assignmentTitle) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("New Assignment");
        notification.setMessage("A new assignment '" + assignmentTitle + "' has been added to the course '" + course.getTitle() + "'");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(NotificationType.ASSIGNMENT_CREATED);
        notification.setLink("/courses/" + course.getId() + "/assignments");
        
        notificationRepository.save(notification);
    }
    
    public void createSubmissionNotification(User instructor, User student, String assignmentTitle) {
        Notification notification = new Notification();
        notification.setUser(instructor);
        notification.setTitle("New Submission");
        notification.setMessage(student.getFirstName() + " " + student.getLastName() + " has submitted their work for the assignment '" + assignmentTitle + "'");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(NotificationType.ASSIGNMENT_CREATED);
        notification.setLink("/assignments/submissions");
        
        notificationRepository.save(notification);
    }
    
    public void createGradingNotification(User student, String assignmentTitle) {
        Notification notification = new Notification();
        notification.setUser(student);
        notification.setTitle("Assignment Graded");
        notification.setMessage("Your submission for '" + assignmentTitle + "' has been graded");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(NotificationType.ASSIGNMENT_GRADED);
        notification.setLink("/assignments/my-submissions");
        
        notificationRepository.save(notification);
    }
    
    public void createCourseEnrollmentNotification(User student, Course course) {
        Notification notification = new Notification();
        notification.setUser(student);
        notification.setTitle("Course Enrollment");
        notification.setMessage("You have been enrolled in the course '" + course.getTitle() + "'");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(NotificationType.COURSE_ENROLLMENT);
        notification.setLink("/courses/" + course.getId());
        
        notificationRepository.save(notification);
    }
    
    private NotificationDto mapToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReadAt(notification.getReadAt());
        dto.setRead(notification.isRead());
        dto.setType(notification.getType());
        dto.setLink(notification.getLink());
        return dto;
    }
}

