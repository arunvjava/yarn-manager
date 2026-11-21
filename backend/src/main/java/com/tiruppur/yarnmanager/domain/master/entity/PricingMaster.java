package com.tiruppur.yarnmanager.domain.master.entity;

import com.tiruppur.yarnmanager.domain.order.entity.Buyer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String priceCode; // e.g. RATE-KNIT-SJ-24, RATE-DYE-DARK, RATE-YARN-30S

    @Column(name = "process_or_item_type", nullable = false, length = 100)
    private String processType; // KNITTING_JOBWORK, DYEING_JOBWORK, YARN_PURCHASE, SEWING_CM, FABRIC_PRINTING

    @Column(nullable = false, length = 150)
    private String categoryOrSpec; // Single Jersey 24GG, Dark Shade (Navy/Black), Bio-Polish, 30s Combed Cotton

    @Column(nullable = false, length = 20)
    private String uomCode; // KG, PCS, MTR

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal baseRate; // Rate amount

    @Column(length = 10, nullable = false)
    @Builder.Default
    private String currency = "INR";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer; // Optional: Buyer-specific negotiated rate, null = standard factory rate

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Column(length = 500)
    private String notes;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
