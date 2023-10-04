package com.example.expenseit.controllers;

import com.example.expenseit.services.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ChatbotControllerTest {

    @InjectMocks
    private ChatbotController chatbotController;

    @Mock
    private ChatbotService chatbotService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChatbotResponse() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        String message = "Hello, Chatbot!";
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("response", "Hello, User!");

        when(chatbotService.handleChatBotCommunication(message, clientId)).thenReturn(responseMap);

        ResponseEntity<Map<String, String>> response = chatbotController.getChatbotResponse(message, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMap, response.getBody());
    }
}
