package com.github.ricardobaumann.vehiclemanager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class Brand {
    @Id
    private UUID id;

    private String name;
}
