package com.example.CQRSPayment.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountDeletedEvent {
    private UUID accountID;
}
