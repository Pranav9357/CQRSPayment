package com.example.CQRSPayment.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentInput {

    private AccountInput sourceAccount;
    private AccountInput receiverAccount;
    private BigDecimal amount;
}
