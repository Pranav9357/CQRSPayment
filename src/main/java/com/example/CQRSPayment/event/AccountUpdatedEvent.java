package com.example.CQRSPayment.event;

import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.BankName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdatedEvent {
    private UUID accountID;
    private String accountNumber;
    private BigDecimal balance;
    private BankName bankName;
    private AccountType accountType;
}
