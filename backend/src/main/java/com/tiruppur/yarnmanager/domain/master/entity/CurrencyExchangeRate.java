package com.tiruppur.yarnmanager.domain.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency_exchange_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String fromCurrency; // e.g. USD, EUR, GBP, INR

    @Column(nullable = false, length = 10)
    private String toCurrency; // e.g. INR, USD, EUR, GBP

    @Column(precision = 14, scale = 6, nullable = false)
    private BigDecimal exchangeRate; // e.g. 86.500000 for 1 USD = 86.50 INR

    @Column(nullable = false)
    private LocalDate effectiveDate; // Date from which this exchange rate applies

    @Column(length = 20)
    private String calendarMonth; // e.g. "2026-10", "2026-11"

    @Column(length = 150)
    private String source; // e.g. "RBI Reference Rate", "Customs Notification", "Interbank Close"

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
