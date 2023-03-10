package com.example.CQRSPayment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private String sourceAccId;
    private String receiverAccId;
    private BigDecimal amount;

}
