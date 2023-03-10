package com.example.CQRSPayment.projection;


import com.example.CQRSPayment.event.MadePaymentEvent;
import com.example.CQRSPayment.exception.AccountNotFoundException;
import com.example.CQRSPayment.exception.InSufficientBalanceException;
import com.example.CQRSPayment.exception.PaymentDataNotFound;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.AllPaymentDetails;
import com.example.CQRSPayment.model.Payment;
import com.example.CQRSPayment.query.FindPaymentDataQuery;
import com.example.CQRSPayment.repository.AccountRepository;
import com.example.CQRSPayment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProjection {

    private final PaymentRepository paymentRepo;
    private final AccountRepository accountRepository;


    //We will define the handler for every emitted Event

    @EventHandler
    public void makeTransaction(MadePaymentEvent event) throws InSufficientBalanceException, AccountNotFoundException {
        log.debug("Handling payment", event.getPaymentId());

        Optional<Account> sourceAccount = accountRepository.findById(UUID.fromString(event.getSourceAccId()));
        Optional<Account> receiverAccount = accountRepository.findById(UUID.fromString(event.getReceiverAccId()));

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setSourceAccId(String.valueOf(sourceAccount.get().getAccountID()));
        payment.setReceiverAccId(String.valueOf(receiverAccount.get().getAccountID()));
        payment.setAmount(event.getAmount());
        updateAccountBalance(sourceAccount.get(), event.getAmount());
        updateAccountBalanceReceive(receiverAccount.get(), event.getAmount());
        paymentRepo.save(payment);
    }

    private void updateAccountBalance(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }
    private void updateAccountBalanceReceive(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    private boolean isAmountAvailable(BigDecimal amount, BigDecimal balance) {
        return balance.subtract(amount).intValue() > 0;
    }

    @QueryHandler
    public Optional<Payment> getPaymentData(FindPaymentDataQuery query) throws PaymentDataNotFound {
        log.debug("Handling Find payment data query", query.getPaymentID());

        return paymentRepo.findById(query.getPaymentID());
    }

    @QueryHandler
    public List<Payment> getAllPayment(AllPaymentDetails allPaymentDetails) {
        log.debug("Handling Find All Account query");

        List<Payment> payments = new ArrayList<>();

        Optional<List<Payment>> optionalPayments = Optional.ofNullable(paymentRepo.findAll());

        if(optionalPayments.isPresent()) {
            for(Payment payment: optionalPayments.get()) {
                Payment response = new Payment();
                BeanUtils.copyProperties(payment, response);
                payments.add(response);
            }
        }

        return payments;
    }

}
