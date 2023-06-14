package com.megafair.model;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountOperationRequest(@Positive BigDecimal amount, String requestId) {
}
