package com.example.CQRSPayment.exception;

public class PaymentDataNotFound extends Exception{
    public PaymentDataNotFound(){
        super("Payment Data Not Found");
    }
}
