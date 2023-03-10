package com.example.CQRSPayment.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakePaymentCommand {

    @TargetAggregateIdentifier
    private UUID paymentId;
    private String sourceAccId;
    private String receiverAccId;
    private BigDecimal amount;
}
