package com.example.expenseit.controllers;

import com.example.expenseit.models.Client;
import com.example.expenseit.models.dto.ClientDTO;
import com.example.expenseit.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginClient() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password");

        Client client = Client.builder().firstName("Kalin").secondName("Dobrinov").thirdName("Borodzhijski").balance(1000).build();
        when(clientService.logInClient("test@example.com", "password")).thenReturn(client);
        ResponseEntity<Map<String, String>> response = clientController.loginClient(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void registerUser() throws Exception {
        String clientJSON = "{"
                + "\"firstName\":\"Test\","
                + "\"secondName\":\"Test\","
                + "\"thirdName\":\"Test\","
                + "\"password\":\"password123\","
                + "\"email\":\"test@example.com\","
                + "\"balance\":1000.0"
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = Client.builder().build();

        when(clientService.registerNewClient(objectMapper.readValue(clientJSON, ClientDTO.class))).thenReturn(client);

        ResponseEntity<Client> response = clientController.registerUser(clientJSON);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }
}
