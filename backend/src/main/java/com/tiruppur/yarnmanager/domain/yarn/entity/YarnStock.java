package com.tiruppur.yarnmanager.domain.yarn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "yarn_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YarnStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String millName;           // e.g. KPR Mill, Ramco, Bannari Amman

    @Column(nullable = false, length = 100)
    private String lotNumber;

    @Column(nullable = false, length = 50)
    private String yarnCount;          // e.g. 30s Combed Cotton, 34s Lycra, 24s Melange

    @Column(nullable = false, length = 50)
    private String yarnType;           // 100% Cotton, Melange, PC Blend, Modal

    @Column(nullable = false)
    private Integer totalBags;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal netWeightKg;    // Available net weight in KG

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerKg;      // In INR

    @Builder.Default
    private LocalDate receivedDate = LocalDate.now();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
