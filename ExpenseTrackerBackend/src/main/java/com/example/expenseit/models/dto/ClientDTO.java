package com.example.expenseit.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {
    private String firstName;
    private String secondName;
    private String thirdName;
    private String password;
    private String email;
    private double balance;
}
