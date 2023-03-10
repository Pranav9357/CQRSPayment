package com.example.CQRSPayment.command;


import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.BankName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountCommand {

    @TargetAggregateIdentifier
    private UUID accountID;
    private String accountNumber;
    private BigDecimal balance;
    private BankName bankName;
    private AccountType accountType;
}
