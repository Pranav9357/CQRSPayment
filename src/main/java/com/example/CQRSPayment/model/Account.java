package com.example.CQRSPayment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account", uniqueConstraints = {@UniqueConstraint(columnNames = {"accountNumber"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private UUID accountID;
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private BankName bankName;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
}

