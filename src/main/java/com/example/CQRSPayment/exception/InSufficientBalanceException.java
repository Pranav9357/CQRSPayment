package com.example.CQRSPayment.exception;

public class InSufficientBalanceException extends Exception{

    public InSufficientBalanceException(){
        super("Cannot Debit desired Amount, You have inSufficient Balance!!");
    }
}
