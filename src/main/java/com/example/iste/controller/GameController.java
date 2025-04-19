package com.example.iste.controller;

import com.example.iste.entity.socket.DrawMessage;
import com.example.iste.entity.socket.GuessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @MessageMapping("/draw") // client -> "/app/draw"
    public void handleDraw(DrawMessage drawMessage) {
        messagingTemplate.convertAndSend("/topic/draw", drawMessage); // broadcast to all
    }

    @MessageMapping("/guess")
    public void handleGuess(GuessMessage guessMessage) {
        messagingTemplate.convertAndSend("/topic/guess", guessMessage);
    }
}
