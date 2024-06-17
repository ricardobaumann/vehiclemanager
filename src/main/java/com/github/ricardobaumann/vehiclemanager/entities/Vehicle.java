package com.github.ricardobaumann.vehiclemanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "vehicles")
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @Column(updatable = false)
    private ZonedDateTime createdAt;
    private Integer year;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    private Integer doorsAmount;
    private String color;
}
