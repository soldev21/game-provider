package com.megafair.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "casino")
public class Casino {
    @Id
    private Long id;
    private UUID casinoId;
    private UUID platformId;
    private String name;
    private Boolean enabled;
}
