package com.trainingmanagement.service;

import com.trainingmanagement.dto.LessonDto;
import com.trainingmanagement.dto.ModuleDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Course;
import com.trainingmanagement.model.Lesson;
import com.trainingmanagement.model.Module;
import com.trainingmanagement.repository.CourseRepository;
import com.trainingmanagement.repository.LessonRepository;
import com.trainingmanagement.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {
    
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    
    @Autowired
    public ModuleService(ModuleRepository moduleRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }
    
    public ModuleDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
        return mapToDto(module);
    }
    
    public List<ModuleDto> getModulesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        return moduleRepository.findByCourseOrderByOrderIndex(course).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public ModuleDto createModule(ModuleDto moduleDto) {
        Course course = courseRepository.findById(moduleDto.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", moduleDto.getCourseId()));
        
        Module module = new Module();
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());
        module.setOrderIndex(moduleDto.getOrderIndex());
        module.setCourse(course);
        
        Module savedModule = moduleRepository.save(module);
        return mapToDto(savedModule);
    }
    
    public ModuleDto updateModule(Long id, ModuleDto moduleDto) {
        Module module = moduleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
        
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());
        module.setOrderIndex(moduleDto.getOrderIndex());
        
        if (moduleDto.getCourseId() != null && !moduleDto.getCourseId().equals(module.getCourse().getId())) {
            Course course = courseRepository.findById(moduleDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", moduleDto.getCourseId()));
            module.setCourse(course);
        }
        
        Module updatedModule = moduleRepository.save(module);
        return mapToDto(updatedModule);
    }
    
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Module", "id", id);
        }
        moduleRepository.deleteById(id);
    }
    
    private ModuleDto mapToDto(Module module) {
        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setOrderIndex(module.getOrderIndex());
        dto.setCourseId(module.getCourse().getId());
        
        List<LessonDto> lessonDtos = lessonRepository.findByModuleOrderByOrderIndex(module).stream()
            .map(this::mapLessonToDto)
            .collect(Collectors.toList());
        
        dto.setLessons(lessonDtos);
        
        return dto;
    }
    
    private LessonDto mapLessonToDto(Lesson lesson) {
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

