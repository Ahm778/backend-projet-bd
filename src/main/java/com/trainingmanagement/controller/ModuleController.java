package com.trainingmanagement.controller;

import com.trainingmanagement.dto.ModuleDto;
import com.trainingmanagement.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {
    
    private final ModuleService moduleService;
    
    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ModuleDto> getModuleById(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.getModuleById(id));
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleDto>> getModulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(moduleService.getModulesByCourse(courseId));
    }
    
    @PostMapping
    public ResponseEntity<ModuleDto> createModule(@RequestBody ModuleDto moduleDto) {
        return ResponseEntity.ok(moduleService.createModule(moduleDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ModuleDto> updateModule(@PathVariable Long id, @RequestBody ModuleDto moduleDto) {
        return ResponseEntity.ok(moduleService.updateModule(id, moduleDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}

