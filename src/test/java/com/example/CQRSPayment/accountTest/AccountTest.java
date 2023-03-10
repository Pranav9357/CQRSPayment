package com.example.CQRSPayment.accountTest;

import com.example.CQRSPayment.aggregate.AccountAggregate;
import com.example.CQRSPayment.command.CreateAccountCommand;
import com.example.CQRSPayment.command.DeleteAccountCommand;
import com.example.CQRSPayment.command.UpdateAccountCommand;
import com.example.CQRSPayment.controller.AccountController;
import com.example.CQRSPayment.dto.AccountCreateDTO;
import com.example.CQRSPayment.dto.AccountUpdateDTO;
import com.example.CQRSPayment.event.AccountCreatedEvent;
import com.example.CQRSPayment.event.AccountDeletedEvent;
import com.example.CQRSPayment.event.AccountUpdatedEvent;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.AccountType;
import com.example.CQRSPayment.model.BankName;
import com.example.CQRSPayment.service.AccountService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountTest {

    private FixtureConfiguration<AccountAggregate> fixtureConfiguration;
    private final UUID id = UUID.fromString("f35123c6-7dc0-4846-bb31-81f591079577");

    @BeforeEach
    public void setUp() {
        fixtureConfiguration = new AggregateTestFixture<>(AccountAggregate.class);
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    AccountCreateDTO accountCreateDTO = new AccountCreateDTO(
            "101",
            BigDecimal.valueOf(2000),
            BankName.HDFC_BANK,
            AccountType.SAVING
    );

    Account account = new Account(
            id,
            "201",
            BigDecimal.valueOf(3000),
            BankName.CANARA_BANK,
            AccountType.SAVING
    );

    Account account1 = new Account(
            id,
            "203",
            BigDecimal.valueOf(4000),
            BankName.SBI,
            AccountType.SAVING
    );


    @Test
    public void createBankAccount() {
        fixtureConfiguration.givenNoPriorActivity()
                .when(new CreateAccountCommand(
                        account.getAccountID(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getBankName(),
                        account.getAccountType()
                ))
                .expectEvents(new AccountCreatedEvent(
                        account.getAccountID(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getBankName(),
                        account.getAccountType()
                ));
    }

    @Test
    public void updateBankAccount() {
        fixtureConfiguration.given(new AccountCreatedEvent(
                        account.getAccountID(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getBankName(),
                        account.getAccountType()
                ))
                .when(new UpdateAccountCommand(
                        account1.getAccountID(),
                        account1.getAccountNumber(),
                        account1.getBalance(),
                        account1.getBankName(),
                        account1.getAccountType()
                ))
                .expectEvents(new AccountUpdatedEvent(
                        account1.getAccountID(),
                        account1.getAccountNumber(),
                        account1.getBalance(),
                        account1.getBankName(),
                        account1.getAccountType()
                ));
    }

    @Test
    public void deleteBankAccount() {
        fixtureConfiguration.given(new AccountCreatedEvent(
                        account.getAccountID(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getBankName(),
                        account.getAccountType()
                ))
                .when(new DeleteAccountCommand(
                        account.getAccountID()
                ))
                .expectEvents(new AccountDeletedEvent(
                        account.getAccountID()
                ));
    }

    @Test
    public void createAccount() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/account/create")
                        .content(asJsonString(accountCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateAccount() throws Exception {

        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO(
                "103",
                BigDecimal.valueOf(3000),
                BankName.SBI,
                AccountType.SAVING
        );

        mvc.perform(MockMvcRequestBuilders.put("/account/update/{id}", id)
                .content(asJsonString(accountUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAccount() throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete("/account/delete/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/account/get/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllAccount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/account/getAllAccount")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
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
