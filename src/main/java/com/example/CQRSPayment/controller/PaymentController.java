package com.example.CQRSPayment.controller;



import com.example.CQRSPayment.dto.PaymentDTO;
import com.example.CQRSPayment.exception.PaymentDataNotFound;
import com.example.CQRSPayment.model.Response;
import com.example.CQRSPayment.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4000/")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/make")
    public ResponseEntity<Object> makePayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            paymentService.makePayment(paymentDTO);
            return Response.generateResponse("Payment Completed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable("id") UUID id) throws Exception{
        try {
            return new ResponseEntity<>(paymentService.findById(id), HttpStatus.OK);
        } catch (PaymentDataNotFound e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllPayment")
    public ResponseEntity<Object> getAllPayment() {
        try {
            return new ResponseEntity<>(paymentService.findAllPayment(), HttpStatus.OK);
        } catch (Exception e) {
            return Response.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
