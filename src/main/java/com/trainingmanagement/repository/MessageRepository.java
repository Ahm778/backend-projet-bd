package com.trainingmanagement.repository;

import com.trainingmanagement.model.Message;
import com.trainingmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrderBySentAtDesc(User sender);
    List<Message> findByRecipientOrderBySentAtDesc(User recipient);
    List<Message> findByRecipientAndIsReadFalseOrderBySentAtDesc(User recipient);
    long countByRecipientAndIsReadFalse(User recipient);
}

