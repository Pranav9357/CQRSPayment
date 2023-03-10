package com.example.CQRSPayment.exception;

public class AccountNotFoundException extends Exception{

    public AccountNotFoundException(){
        super("Cannot found Account");
    }
}
