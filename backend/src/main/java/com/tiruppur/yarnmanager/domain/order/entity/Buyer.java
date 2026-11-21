package com.tiruppur.yarnmanager.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name; // Legal company name

    @Column(nullable = false, unique = true, length = 50)
    private String buyerCode; // e.g., 'BUY-ZARA-01'

    @Column(length = 200)
    private String brandNames; // e.g. 'Zara Man, Zara Kids, TRF'

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String country = "India";

    @Column(length = 10, nullable = false)
    @Builder.Default
    private String currency = "USD"; // USD, EUR, GBP, INR

    @Column(length = 100)
    private String paymentTerms; // e.g. 'LC at 60 Days', 'Net 30 Days'

    @Column(length = 50)
    private String taxIdGstin; // GSTIN / VAT registration number

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 150)
    private String contactEmail;

    @Column(length = 50)
    private String contactPhone;

    @Column(length = 500)
    private String billingAddress;

    @Column(length = 150)
    private String shippingPort; // e.g., 'Tuticorin Port (VOCPT)', 'Chennai Port'

    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal tolerancePercentage = new BigDecimal("5.00"); // +/- 5% shipping allowance

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
