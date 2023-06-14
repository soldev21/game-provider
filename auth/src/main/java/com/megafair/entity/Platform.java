package com.megafair.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "platforms")
public class Platform {
    @Id
    private Long id;
    private UUID platformId;
    private String name;
    private Boolean enabled;
    private String token;
}
