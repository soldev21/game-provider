package com.megafair.model;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SessionTokenRequest {
    // ids type can be different regarding the internal standards of Platforms
    private UUID casinoId;
    private UUID gameId;
    @Positive(message = "budget should be greater than zero")
    private BigDecimal budget;
    private GameUser user;

    // these fields can be filtered to keep some of them
    public record GameUser(String name, String surname, String nickname, String avatarBase64) {}
}
