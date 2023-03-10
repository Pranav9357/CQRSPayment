package com.example.CQRSPayment.projection;

import com.example.CQRSPayment.event.AccountCreatedEvent;
import com.example.CQRSPayment.event.AccountDeletedEvent;
import com.example.CQRSPayment.event.AccountUpdatedEvent;
import com.example.CQRSPayment.exception.AccountNotCreateException;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.AllAccountDetails;
import com.example.CQRSPayment.model.BankName;
import com.example.CQRSPayment.query.FindAccountQuery;
import com.example.CQRSPayment.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountProjection {

    private final AccountRepository accountRepository;

    @EventHandler
    public void createAccount(AccountCreatedEvent event) throws AccountNotCreateException {
        log.debug("Handling a Account creation command..",event.getAccountID());

        Optional<Account> accountOptional = accountRepository.findById(event.getAccountID());

        if(accountOptional.isPresent()) {
            throw new AccountNotCreateException();
        } else {
            Account account = new Account(
                    event.getAccountID(),
                    event.getAccountNumber(),
                    event.getBalance(),
                    BankName.valueOf(event.getBankName().toString()),
                    AccountType.valueOf(event.getAccountType().toString())
            );

            accountRepository.save(account);
        }
    }

    @EventHandler
    public void updateAccount(AccountUpdatedEvent event) {

        log.debug("Handling a Account creation command..", event.getAccountID());

        Account account = new Account(
                event.getAccountID(),
                event.getAccountNumber(),
                event.getBalance(),
                BankName.valueOf(event.getBankName().toString()),
                AccountType.valueOf(event.getAccountType().toString())
        );
        accountRepository.save(account);
    }

    @EventHandler
    public void deleteAccount(AccountDeletedEvent event) {

        accountRepository.deleteById(event.getAccountID());
    }

    @QueryHandler
    public Account getAccountData(FindAccountQuery query)  {
        log.debug("Handling Find Account query", query.getAccountID());

        return accountRepository.findById(query.getAccountID()).orElse(null);
    }

    @QueryHandler
    public List<Account> getAllAccount(AllAccountDetails allAccountDetails) {
        log.debug("Handling Find All Account query");

        List<Account> accounts = new ArrayList<>();

        Optional<List<Account>> optionalAccounts = Optional.ofNullable(accountRepository.findAll());

        if(optionalAccounts.isPresent()) {
            for(Account account: optionalAccounts.get()) {
                Account response = new Account();
                BeanUtils.copyProperties(account, response);
                accounts.add(response);
            }
        }

        return accounts;
    }

}
