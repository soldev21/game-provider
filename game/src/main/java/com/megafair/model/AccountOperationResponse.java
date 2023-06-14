package com.megafair.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountOperationResponse {
    private Response response;
    private Payload data;

    public record Payload(BigDecimal budget) {}
}
