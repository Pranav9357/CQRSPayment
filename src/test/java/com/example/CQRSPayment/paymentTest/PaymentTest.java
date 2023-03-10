package com.example.CQRSPayment.paymentTest;

import com.example.CQRSPayment.aggregate.PaymentAggregate;
import com.example.CQRSPayment.command.MakePaymentCommand;
import com.example.CQRSPayment.controller.PaymentController;
import com.example.CQRSPayment.dto.PaymentDTO;
import com.example.CQRSPayment.event.MadePaymentEvent;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.BankName;
import com.example.CQRSPayment.model.Payment;
import com.example.CQRSPayment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    private FixtureConfiguration<PaymentAggregate> fixtureConfiguration;
    private final UUID id = UUID.fromString("f35123c6-7dc0-4846-bb31-81f591079577");
    private final UUID id1 = UUID.fromString("583c765d-e692-459b-ab42-4d135b211bd7");
    private final UUID id2 = UUID.fromString("c0c78f26-0336-4d17-8ec3-3e0fd16d45b8");

    @BeforeEach
    public void setUp() {
        fixtureConfiguration = new AggregateTestFixture<>(PaymentAggregate.class);
    }

    Account account = new Account(
            id1,
            "101",
            BigDecimal.valueOf(2000),
            BankName.HDFC_BANK,
            AccountType.SAVING
    );

    Account account1 = new Account(
            id2,
            "103",
            BigDecimal.valueOf(3000),
            BankName.SBI,
            AccountType.SAVING
    );

    Payment payment = new Payment(
            id,
            account.getAccountID().toString(),
            account1.getAccountID().toString(),
            BigDecimal.valueOf(2000)
    );

    PaymentDTO paymentDTO = new PaymentDTO(
            account.getAccountID().toString(),
            account1.getAccountID().toString(),
            BigDecimal.valueOf(500)
    );

    @Test
    public void makePayment() {
        fixtureConfiguration.givenNoPriorActivity()
                .when(new MakePaymentCommand(
                        payment.getPaymentId(),
                        payment.getSourceAccId(),
                        payment.getReceiverAccId(),
                        payment.getAmount()
                ))
                .expectEvents(new MadePaymentEvent(
                        payment.getPaymentId(),
                        payment.getSourceAccId(),
                        payment.getReceiverAccId(),
                        payment.getAmount()
                ));

    }

    @Test
    public void makePaymentMethod() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/payment/make")
                        .content(asJsonString(payment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllPayment() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/payment/getAllPayment")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void findById() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/payment/get/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
