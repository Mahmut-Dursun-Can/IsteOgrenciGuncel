package com.example.iste.service;

import com.example.iste.entity.socket.ChatMessage;
import com.example.iste.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // Mesajı veritabanına kaydetme
    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(String.valueOf(LocalDateTime.now())); // Mesaj zamanını şimdiki zaman olarak set ediyoruz.
        chatMessageRepository.save(chatMessage);
    }


}
