package com.example.CQRSPayment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountInput {

    @NotBlank(message = "Account id is mandatory")
    private UUID id;
}
