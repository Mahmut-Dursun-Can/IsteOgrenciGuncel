package com.example.iste.repository;

import com.example.iste.entity.socket.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Gönderici ve alıcı kullanıcı adına göre mesajları filtreleyen metod
    List<ChatMessage> findBySenderUsernameAndRecipientUsername(String senderUsername, String recipientUsername);
}
