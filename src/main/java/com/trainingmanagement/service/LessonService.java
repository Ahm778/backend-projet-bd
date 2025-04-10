package com.trainingmanagement.service;

import com.trainingmanagement.dto.LessonDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Lesson;
import com.trainingmanagement.model.Module;
import com.trainingmanagement.repository.LessonRepository;
import com.trainingmanagement.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    
    @Autowired
    public LessonService(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }
    
    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));
        return mapToDto(lesson);
    }
    
    public List<LessonDto> getLessonsByModule(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
            .orElseThrow(() -> new ResourceNotFoundException("Module", "id", moduleId));
        
        return lessonRepository.findByModuleOrderByOrderIndex(module).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public LessonDto createLesson(LessonDto lessonDto) {
        Module module = moduleRepository.findById(lessonDto.getModuleId())
            .orElseThrow(() -> new ResourceNotFoundException("Module", "id", lessonDto.getModuleId()));
        
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setOrderIndex(lessonDto.getOrderIndex());
        lesson.setType(lessonDto.getType());
        lesson.setResourceUrl(lessonDto.getResourceUrl());
        lesson.setModule(module);
        
        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToDto(savedLesson);
    }
    
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));
        
        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setOrderIndex(lessonDto.getOrderIndex());
        lesson.setType(lessonDto.getType());
        lesson.setResourceUrl(lessonDto.getResourceUrl());
        
        if (lessonDto.getModuleId() != null && !lessonDto.getModuleId().equals(lesson.getModule().getId())) {
            Module module = moduleRepository.findById(lessonDto.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", lessonDto.getModuleId()));
            lesson.setModule(module);
        }
        
        Lesson updatedLesson = lessonRepository.save(lesson);
        return mapToDto(updatedLesson);
    }
    
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson", "id", id);
        }
        lessonRepository.deleteById(id);
    }
    
    private LessonDto mapToDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setOrderIndex(lesson.getOrderIndex());
        dto.setType(lesson.getType());
        dto.setResourceUrl(lesson.getResourceUrl());
        dto.setModuleId(lesson.getModule().getId());
        return dto;
    }
}

