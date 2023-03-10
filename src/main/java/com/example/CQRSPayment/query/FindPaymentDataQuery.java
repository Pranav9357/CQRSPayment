package com.example.CQRSPayment.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPaymentDataQuery {

    private UUID paymentID;
}
