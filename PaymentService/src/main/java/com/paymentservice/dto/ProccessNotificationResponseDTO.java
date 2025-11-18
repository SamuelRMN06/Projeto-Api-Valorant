package com.paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProccessNotificationResponseDTO {

    boolean success;
    String updateStatus;
}
