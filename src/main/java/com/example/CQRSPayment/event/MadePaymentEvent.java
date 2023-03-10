package com.example.CQRSPayment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MadePaymentEvent {
    private UUID paymentId;
    private String sourceAccId;
    private String receiverAccId;
    private BigDecimal amount;

}
