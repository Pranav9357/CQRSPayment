package com.example.CQRSPayment.exception;

public class AccountNotCreateException extends Exception{

    public AccountNotCreateException(){
        super("Cannot create Account");
    }
}
