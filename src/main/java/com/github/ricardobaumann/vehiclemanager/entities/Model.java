package com.github.ricardobaumann.vehiclemanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "models")
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    private String name;
    private BigDecimal price;
}
