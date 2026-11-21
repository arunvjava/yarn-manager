package com.tiruppur.yarnmanager.domain.master.dto;

import com.tiruppur.yarnmanager.domain.master.entity.PricingMaster;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolvedRateDto {
    private PricingMaster pricingMaster;
    private LocalDate effectiveDate;
    private BigDecimal baseRate;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal convertedRate;
    private BigDecimal exchangeRateUsed;
    private String note;
}
