package com.tiruppur.yarnmanager.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineSummaryDto {
    private BigDecimal totalYarnInwardKg;
    private BigDecimal totalKnittedFabricKg;
    private BigDecimal totalDyedFabricKg;
    private Integer totalCutPieces;
    private Integer totalSewnGarments;
    private Integer totalRejectedGarments;
    private Integer totalPackedGarments;
    private BigDecimal overallWastagePercentage;
}
