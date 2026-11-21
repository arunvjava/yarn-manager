package com.tiruppur.yarnmanager.domain.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "uom_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UomMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String uomCode; // KG, BAG, PCS, MTR, ROLL, CONE, CTN, DOZ, LBS, YDS

    @Column(nullable = false, length = 100)
    private String uomName; // Kilogram, Yarn Bag (45.36 kg / 100 lbs), Pieces, etc.

    @Column(nullable = false, length = 50)
    private String category; // WEIGHT, COUNT, LENGTH, PACKAGING

    @Builder.Default
    private boolean baseUom = false; // Is this the base reference unit for the category?

    @Column(precision = 12, scale = 6, nullable = false)
    @Builder.Default
    private BigDecimal conversionFactor = BigDecimal.ONE; // Factor to convert to base unit

    @Builder.Default
    private int decimalPlaces = 2;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
