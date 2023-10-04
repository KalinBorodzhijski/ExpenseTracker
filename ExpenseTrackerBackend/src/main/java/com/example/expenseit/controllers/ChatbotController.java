package com.example.expenseit.controllers;

import com.example.expenseit.services.ChatbotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> getChatbotResponse(@RequestBody String message, HttpServletRequest request) {
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(chatbotService.handleChatBotCommunication(message,clientId), HttpStatus.OK);
    }
}
