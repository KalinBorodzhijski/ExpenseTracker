package com.example.expenseit.controllers;

import com.example.expenseit.models.dto.NetWorthDTO;
import com.example.expenseit.services.NetWorthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/networth")
public class NetWorthController {

    private final NetWorthService netWorthService;

    public NetWorthController(NetWorthService netWorthService) {
        this.netWorthService = netWorthService;
    }

    @GetMapping()
    public ResponseEntity<NetWorthDTO> getNetWorthAndPredicted(HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        NetWorthDTO netWorthDTO = NetWorthDTO.builder()
                .actualNetWorth(netWorthService.getMonthlyNetWorth(clientId))
                .predictedNetWorth(netWorthService.getPredictedNetWorth(clientId))
                .build();
        return new ResponseEntity<>(netWorthDTO, HttpStatus.OK);
    }
}
