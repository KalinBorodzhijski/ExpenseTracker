package com.example.expenseit.controllers;

import com.example.expenseit.models.dto.NetWorthDTO;
import com.example.expenseit.services.NetWorthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NetWorthControllerTest {

    @InjectMocks
    private NetWorthController netWorthController;

    @Mock
    private NetWorthService netWorthService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNetWorthAndPredicted() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<YearMonth, Double> actualNetWorthMap = new HashMap<>();
        actualNetWorthMap.put(YearMonth.now(), 100.0);

        Map<YearMonth, Double> predictedNetWorthMap = new HashMap<>();
        predictedNetWorthMap.put(YearMonth.now(), 200.0);

        NetWorthDTO netWorthDTO = NetWorthDTO.builder()
                .actualNetWorth(actualNetWorthMap)
                .predictedNetWorth(predictedNetWorthMap)
                .build();

        when(netWorthService.getMonthlyNetWorth(clientId)).thenReturn(actualNetWorthMap);
        when(netWorthService.getPredictedNetWorth(clientId)).thenReturn(predictedNetWorthMap);

        ResponseEntity<NetWorthDTO> response = netWorthController.getNetWorthAndPredicted(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(netWorthDTO.getActualNetWorth(), Objects.requireNonNull(response.getBody()).getActualNetWorth());
        assertEquals(netWorthDTO.getPredictedNetWorth(), response.getBody().getPredictedNetWorth());
    }
}
