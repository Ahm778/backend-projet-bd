package com.trainingmanagement.controller;

import com.trainingmanagement.dto.MessageDto;
import com.trainingmanagement.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    private final MessageService messageService;
    
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessageDto>> getSentMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getSentMessages(userId));
    }
    
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MessageDto>> getReceivedMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getReceivedMessages(userId));
    }
    
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<MessageDto>> getUnreadMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getUnreadMessages(userId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }
    
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto) {
        return ResponseEntity.ok(messageService.sendMessage(messageDto));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<MessageDto> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.markAsRead(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}

