package com.megafair.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCache {

    public GameCache(String id, String url) {
        this(UUID.fromString(id), url);
    }

    private UUID id;
    private String url;
}
