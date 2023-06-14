package com.megafair.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GameCache {
    private UUID id;
    private String url;
}
