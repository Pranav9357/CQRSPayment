package com.example.CQRSPayment.service;

import com.example.CQRSPayment.command.CreateAccountCommand;
import com.example.CQRSPayment.command.DeleteAccountCommand;
import com.example.CQRSPayment.command.UpdateAccountCommand;
import com.example.CQRSPayment.dto.AccountCreateDTO;
import com.example.CQRSPayment.dto.AccountUpdateDTO;
import com.example.CQRSPayment.exception.AccountNotCreateException;
import com.example.CQRSPayment.exception.AccountNotFoundException;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.AllAccountDetails;
import com.example.CQRSPayment.model.BankName;
import com.example.CQRSPayment.query.FindAccountQuery;
import com.example.CQRSPayment.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AccountService {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    private final AccountRepository accountRepository;

    public CompletableFuture<Account> createAccount(AccountCreateDTO accountDTO) throws AccountNotCreateException {

        if(accountDTO.getBalance().intValue() >= 1000) {
            return commandGateway.send(new CreateAccountCommand(
                    UUID.randomUUID(),
                    accountDTO.getAccountNumber(),
                    accountDTO.getBalance(),
                    BankName.valueOf(accountDTO.getBankName().toString()),
                    AccountType.valueOf(accountDTO.getAccountType().toString())
            ));
        } else {
            throw new AccountNotCreateException();
        }
    }

    public CompletableFuture<Account> updateAccount(UUID id, AccountUpdateDTO accountDTO) throws AccountNotFoundException {

        Optional<Account> accountCompletableFuture = accountRepository.findById(id);

        if(accountCompletableFuture.isPresent()) {
            return commandGateway.send(new UpdateAccountCommand(
                    id,
                    accountDTO.getAccountNumber(),
                    accountDTO.getBalance(),
                    BankName.valueOf(accountDTO.getBankName().toString()),
                    AccountType.valueOf(accountDTO.getAccountType().toString())
            ));
        } else {
            throw new AccountNotFoundException();
        }
    }

    public void deleteAccount(UUID id) throws AccountNotFoundException {
        Optional<Account> accountCompletableFuture = accountRepository.findById(id);
        if(accountCompletableFuture.isPresent()) {
            commandGateway.send(new DeleteAccountCommand(
                    id
            ));
        } else {
            throw new AccountNotFoundException();
        }
    }

    public Account findById(UUID id) throws AccountNotFoundException {
        Optional<Account> accountCompletableFuture = accountRepository.findById(id);
        if(accountCompletableFuture.isPresent()) {
            return queryGateway.query(new FindAccountQuery(id),
                    ResponseTypes.instanceOf(Account.class)).join();
        } else {
            throw new AccountNotFoundException();
        }

    }

    public List<Account> findAllAccount() throws AccountNotFoundException {

        List<Account> accounts = accountRepository.findAll();

        if(!accounts.isEmpty()) {
            return queryGateway.query(new AllAccountDetails(),
                            ResponseTypes.multipleInstancesOf(Account.class))
                    .join();
        } else {
            throw new AccountNotFoundException();
        }
    }

}
