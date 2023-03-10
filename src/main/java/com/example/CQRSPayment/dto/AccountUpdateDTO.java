package com.example.CQRSPayment.dto;


import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.BankName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDTO {
    private String accountNumber;
    private BigDecimal balance;
    private BankName bankName;
    private AccountType accountType;
}
