package com.example.CQRSPayment.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAccountCommand {

    @TargetAggregateIdentifier
    private UUID accountID;
}
