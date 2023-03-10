package com.example.CQRSPayment.aggregate;

import com.example.CQRSPayment.command.MakePaymentCommand;
import com.example.CQRSPayment.event.MadePaymentEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAggregate {
    @AggregateIdentifier
    private UUID paymentId;
    private UUID sourceAccId;
    private UUID receiverAccId;
    private BigDecimal amount;

    @CommandHandler
    public PaymentAggregate(MakePaymentCommand command){

        AggregateLifecycle.apply(new MadePaymentEvent(
                command.getPaymentId(),
                command.getSourceAccId(),
                command.getReceiverAccId(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void makePayment(MadePaymentEvent event){
        this.paymentId = event.getPaymentId();
        this.sourceAccId = UUID.fromString(event.getSourceAccId());
        this.receiverAccId = UUID.fromString(event.getReceiverAccId());
        this.amount = event.getAmount();
    }

}
