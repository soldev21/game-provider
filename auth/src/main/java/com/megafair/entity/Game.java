package com.megafair.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    @Id
    private Long id;
    private String name;
    private UUID gameId;
}
