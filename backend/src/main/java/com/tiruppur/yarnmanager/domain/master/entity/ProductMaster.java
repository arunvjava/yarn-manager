package com.tiruppur.yarnmanager.domain.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String productCode; // e.g. "STY-CREW-180", "STY-POLO-PIQUE-220"

    @Column(nullable = false, length = 150)
    private String styleName; // e.g. "Men Bio-Wash Crew Neck T-Shirt"

    @Column(nullable = false, length = 50)
    private String category; // MEN_ROUND_NECK, MEN_POLO, WOMEN_TOP, KIDS_TEE, HOODIE, JOGGERS

    @Column(nullable = false, length = 100)
    private String fabricType; // Single Jersey, Pique Honeycomb, 1x1 Rib, French Terry

    @Column(nullable = false, length = 50)
    private String yarnCount; // 30s Combed Cotton, 34s Lycra Spun, 24s Melange Cotton

    @Column(nullable = false)
    private Integer targetGsm; // e.g. 180, 220, 240

    @Column(length = 100)
    private String neckType; // "Round Neck (Rib Collar)", "Polo Collar with Placket", "V-Neck"

    @Column(length = 100)
    private String sleeveType; // "Half Sleeve", "Full Sleeve", "Sleeveless"

    @Column(precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal standardSmv = new BigDecimal("12.50"); // Standard Minute Value (sewing SAM)

    @Column(length = 150)
    @Builder.Default
    private String availableSizes = "S, M, L, XL, 2XL";

    @Column(length = 20)
    @Builder.Default
    private String hsnCode = "61091000"; // HSN 6109 for Cotton T-Shirts

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
