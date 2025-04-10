package com.trainingmanagement.service;

import com.trainingmanagement.dto.MessageDto;
import com.trainingmanagement.dto.UserDto;
import com.trainingmanagement.exception.ResourceNotFoundException;
import com.trainingmanagement.model.Message;
import com.trainingmanagement.model.User;
import com.trainingmanagement.repository.MessageRepository;
import com.trainingmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    
    public List<MessageDto> getSentMessages(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return messageRepository.findBySenderOrderBySentAtDesc(user).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<MessageDto> getReceivedMessages(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return messageRepository.findByRecipientOrderBySentAtDesc(user).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<MessageDto> getUnreadMessages(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return messageRepository.findByRecipientAndIsReadFalseOrderBySentAtDesc(user).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public MessageDto getMessageById(Long id) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
        return mapToDto(message);
    }
    
    public MessageDto sendMessage(MessageDto messageDto) {
        User sender = userRepository.findById(messageDto.getSender().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", messageDto.getSender().getId()));
        
        User recipient = userRepository.findById(messageDto.getRecipient().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", messageDto.getRecipient().getId()));
        
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setSubject(messageDto.getSubject());
        message.setContent(messageDto.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        
        Message savedMessage = messageRepository.save(message);
        
        // Create notification for recipient
        notificationService.createMessageNotification(recipient, sender, message.getSubject());
        
        return mapToDto(savedMessage);
    }
    
    public MessageDto markAsRead(Long id) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
        
        if (!message.isRead()) {
            message.setRead(true);
            message.setReadAt(LocalDateTime.now());
            message = messageRepository.save(message);
        }
        
        return mapToDto(message);
    }
    
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message", "id", id);
        }
        messageRepository.deleteById(id);
    }
    
    private MessageDto mapToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        
        UserDto senderDto = new UserDto();
        senderDto.setId(message.getSender().getId());
        senderDto.setEmail(message.getSender().getEmail());
        senderDto.setFirstName(message.getSender().getFirstName());
        senderDto.setLastName(message.getSender().getLastName());
        senderDto.setProfileImage(message.getSender().getProfileImage());
        senderDto.setRole(message.getSender().getRole());
        
        UserDto recipientDto = new UserDto();
        recipientDto.setId(message.getRecipient().getId());
        recipientDto.setEmail(message.getRecipient().getEmail());
        recipientDto.setFirstName(message.getRecipient().getFirstName());
        recipientDto.setLastName(message.getRecipient().getLastName());
        recipientDto.setProfileImage(message.getRecipient().getProfileImage());
        recipientDto.setRole(message.getRecipient().getRole());
        
        dto.setSender(senderDto);
        dto.setRecipient(recipientDto);
        dto.setSubject(message.getSubject());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        dto.setReadAt(message.getReadAt());
        dto.setRead(message.isRead());
        
        return dto;
    }
}

