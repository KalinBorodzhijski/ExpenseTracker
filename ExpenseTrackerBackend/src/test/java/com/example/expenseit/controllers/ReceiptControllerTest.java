package com.example.expenseit.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.expenseit.Constants.PYTHON_SERVICE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReceiptControllerTest {

    @InjectMocks
    private ReceiptController receiptController;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleFileUpload() throws IOException {
        Path path = Paths.get("src/test/resources/test.jpg");
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", fileResource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String expectedResponse = "{\"date\":\"08-08-2023\",\"products\":\"\\u041b\\u0435\\u043a\\u0430\\u0440\\u0441\\u0442\\u0432\\u0430, \\u0425\\u0440\\u0430\\u043d\\u0438\",\"total\":\"89.20\"}\n";
        when(restTemplate.postForEntity(PYTHON_SERVICE_URL + "/scan_receipt", requestEntity, String.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = receiptController.handleFileUpload(file);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

