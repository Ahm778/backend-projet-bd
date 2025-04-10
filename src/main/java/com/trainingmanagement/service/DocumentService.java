package com.trainingmanagement.service;

import com.trainingmanagement.dto.DocumentDto;
import com.trainingmanagement.dto.UserDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.Document;
import com.trainingmanagement.model.User;
import com.trainingmanagement.repository.CourseRepository;
import com.trainingmanagement.repository.DocumentRepository;
import com.trainingmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService;
    
    @Autowired
    public DocumentService(
            DocumentRepository documentRepository,
            UserRepository userRepository,
            CourseRepository courseRepository,
            FileStorageService fileStorageService) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.fileStorageService = fileStorageService;
    }
    
    public DocumentDto getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
        return mapToDto(document);
    }
    
    public List<DocumentDto> getDocumentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        return documentRepository.findByCourse(course).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<DocumentDto> getDocumentsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return documentRepository.findByUploader(user).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public DocumentDto uploadDocument(MultipartFile file, String name, String description, Long courseId, Long uploaderId) {
        User uploader = userRepository.findById(uploaderId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", uploaderId));
        
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        String fileName = fileStorageService.storeFile(file);
        
        Document document = new Document();
        document.setName(name);
        document.setDescription(description);
        document.setFilePath(fileName);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setUploader(uploader);
        document.setCourse(course);
        document.setUploadedAt(LocalDateTime.now());
        
        Document savedDocument = documentRepository.save(document);
        return mapToDto(savedDocument);
    }
    
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
        
        fileStorageService.deleteFile(document.getFilePath());
        documentRepository.delete(document);
    }
    
    public ResponseEntity<byte[]> downloadDocument(Long id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
        
        Resource resource = fileStorageService.loadFileAsResource(document.getFilePath());
        
        try {
            byte[] content = resource.getInputStream().readAllBytes();
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(content);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + document.getFilePath(), e);
        }
    }
    
    private DocumentDto mapToDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setName(document.getName());
        dto.setDescription(document.getDescription());
        dto.setFilePath(document.getFilePath());
        dto.setFileType(document.getFileType());
        dto.setFileSize(document.getFileSize());
        
        UserDto uploaderDto = new UserDto();
        uploaderDto.setId(document.getUploader().getId());
        uploaderDto.setEmail(document.getUploader().getEmail());
        uploaderDto.setFirstName(document.getUploader().getFirstName());
        uploaderDto.setLastName(document.getUploader().getLastName());
        uploaderDto.setProfileImage(document.getUploader().getProfileImage());
        uploaderDto.setRole(document.getUploader().getRole());
        
        dto.setUploader(uploaderDto);
        dto.setCourseId(document.getCourse().getId());
        dto.setUploadedAt(document.getUploadedAt());
        
        return dto;
    }
}

