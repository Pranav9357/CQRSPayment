package com.example.CQRSPayment.aggregate;

import com.example.CQRSPayment.command.CreateAccountCommand;
import com.example.CQRSPayment.command.DeleteAccountCommand;
import com.example.CQRSPayment.command.UpdateAccountCommand;
import com.example.CQRSPayment.event.AccountCreatedEvent;
import com.example.CQRSPayment.event.AccountDeletedEvent;
import com.example.CQRSPayment.event.AccountUpdatedEvent;
import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.BankName;
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
@NoArgsConstructor
@AllArgsConstructor
@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private UUID accountID;
    private String accountNumber;
    private BigDecimal balance;
    private BankName bankName;
    private AccountType accountType;

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getAccountID(),
                command.getAccountNumber(),
                command.getBalance(),
                BankName.valueOf(command.getBankName().toString()),
                AccountType.valueOf(command.getAccountType().toString())
        ));
    }

    @EventSourcingHandler
    public void createEvent(AccountCreatedEvent event) {
        this.accountID = event.getAccountID();
        this.accountNumber = event.getAccountNumber();
        this.balance = event.getBalance();
        this.bankName = BankName.valueOf(event.getBankName().toString());
        this.accountType = AccountType.valueOf(event.getAccountType().toString());
    }

    @CommandHandler
    public void updateAccount(UpdateAccountCommand command) {
        AggregateLifecycle.apply(new AccountUpdatedEvent(
                command.getAccountID(),
                command.getAccountNumber(),
                command.getBalance(),
                BankName.valueOf(command.getBankName().toString()),
                AccountType.valueOf(command.getAccountType().toString())
        ));
    }

    @EventSourcingHandler
    public void updateEvent(AccountUpdatedEvent event) {
        this.accountID = event.getAccountID();
        this.accountNumber = event.getAccountNumber();
        this.balance = event.getBalance();
        this.bankName = BankName.valueOf(event.getBankName().toString());
        this.accountType = AccountType.valueOf(event.getAccountType().toString());
    }

    @CommandHandler
    public void deleteAccount(DeleteAccountCommand command) {
        AggregateLifecycle.apply(new AccountDeletedEvent(
                command.getAccountID()
        ));
    }

    @EventSourcingHandler
    public void deleteEvent(AccountDeletedEvent event) {
        this.accountID = event.getAccountID();
    }
}
