package com.example.CQRSPayment.controller;

import com.example.CQRSPayment.dto.AccountCreateDTO;
import com.example.CQRSPayment.dto.AccountUpdateDTO;
import com.example.CQRSPayment.exception.AccountNotCreateException;
import com.example.CQRSPayment.model.Account;
import com.example.CQRSPayment.model.Response;
import com.example.CQRSPayment.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4000/")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Object> createAccount(@RequestBody AccountCreateDTO accountCreateDTO) {
        try {
            CompletableFuture<Account> account = accountService.createAccount(accountCreateDTO);
            return Response.generateResponse("Account is Created!!", HttpStatus.CREATED);
        } catch (AccountNotCreateException e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable("id") UUID id, @RequestBody AccountUpdateDTO accountUpdateDTO) {
        try {
            accountService.updateAccount(id, accountUpdateDTO);
            return Response.generateResponse("Account is Updated!!", HttpStatus.OK);
        } catch (Exception e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable("id") UUID id) {
        try {
            accountService.deleteAccount(id);
            return Response.generateResponse("Account is Deleted!!", HttpStatus.OK);
        } catch (Exception e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable("id") UUID id) {
        try {
            return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllAccount")
    public ResponseEntity<Object> getAllAccount() {
        try {
            return new ResponseEntity<>(accountService.findAllAccount(), HttpStatus.OK);
        } catch (Exception e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
