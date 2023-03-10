package com.example.CQRSPayment.service;

import com.example.CQRSPayment.command.MakePaymentCommand;
import com.example.CQRSPayment.dto.PaymentDTO;
import com.example.CQRSPayment.exception.AccountNotFoundException;
import com.example.CQRSPayment.exception.InSufficientBalanceException;
import com.example.CQRSPayment.exception.PaymentDataNotFound;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.AllPaymentDetails;
import com.example.CQRSPayment.model.Payment;
import com.example.CQRSPayment.query.FindPaymentDataQuery;
import com.example.CQRSPayment.repository.AccountRepository;
import com.example.CQRSPayment.repository.PaymentRepository;
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
public class PaymentService {        //will dispatch the command to the Axon Engine
    private final CommandGateway commandGateway;    // to dispatch command we have CommandGateway interface
    private final QueryGateway queryGateway;
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    public CompletableFuture<Payment> makePayment(PaymentDTO paymentDTO) throws Exception {

        Optional<Account> accountCompletableFuture = accountRepository.findById(UUID.fromString(paymentDTO.getSourceAccId()));
        Optional<Account> completableFuture = accountRepository.findById(UUID.fromString(paymentDTO.getReceiverAccId()));

        if(accountCompletableFuture.isPresent() && completableFuture.isPresent()) {
            if(accountCompletableFuture.get().getBalance().subtract(paymentDTO.getAmount()).intValue() > 0) {
                return commandGateway.send(new MakePaymentCommand(
                        UUID.randomUUID(),
                        paymentDTO.getSourceAccId(),
                        paymentDTO.getReceiverAccId(),
                        paymentDTO.getAmount()
                ));
            } else {
                throw new InSufficientBalanceException();
            }
        } else {
            throw new AccountNotFoundException();
        }
    }

    public CompletableFuture<Payment> findById(UUID id) throws PaymentDataNotFound {

        Optional<Payment> payment = paymentRepository.findById(id);
        if(!payment.isEmpty()) {
            return queryGateway.query(new FindPaymentDataQuery(id),
                    ResponseTypes.instanceOf(Payment.class));
        } else {
            throw new PaymentDataNotFound();
        }

    }

    public List<Payment> findAllPayment() throws PaymentDataNotFound {

        List<Payment> payments = paymentRepository.findAll();

        if(!payments.isEmpty()) {
            return queryGateway.query(new AllPaymentDetails(),
                            ResponseTypes.multipleInstancesOf(Payment.class))
                    .join();
        } else {
            throw new PaymentDataNotFound();
        }
    }
}
