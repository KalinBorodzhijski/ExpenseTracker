package com.example.expenseit.models.dto;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IntentChatbotResponse {
    private String intent;
    private Map<String, String> entities;
}
