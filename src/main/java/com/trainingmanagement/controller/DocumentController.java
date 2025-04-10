package com.trainingmanagement.controller;

import com.trainingmanagement.dto.DocumentDto;
import com.trainingmanagement.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    
    private final DocumentService documentService;
    
    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<DocumentDto>> getDocumentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(documentService.getDocumentsByCourse(courseId));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentDto>> getDocumentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(documentService.getDocumentsByUser(userId));
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("courseId") Long courseId,
            @RequestParam("uploaderId") Long uploaderId) {
        return ResponseEntity.ok(documentService.uploadDocument(file, name, description, courseId, uploaderId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        return documentService.downloadDocument(id);
    }
}

