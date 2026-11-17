package com.tiruppur.yarnmanager.domain.dashboard.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.cutting.service.CuttingService;
import com.tiruppur.yarnmanager.domain.dashboard.dto.PipelineSummaryDto;
import com.tiruppur.yarnmanager.domain.dyeing.repository.DyeingBatchRepository;
import com.tiruppur.yarnmanager.domain.knitting.service.KnittingService;
import com.tiruppur.yarnmanager.domain.packing.service.PackingService;
import com.tiruppur.yarnmanager.domain.sewing.service.SewingService;
import com.tiruppur.yarnmanager.domain.yarn.service.YarnStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Executive Dashboard", description = "Plant-wide metrics tracking yarn conversion efficiency, stage KPIs, and wastage")
public class DashboardController {

    private final YarnStockService yarnService;
    private final KnittingService knittingService;
    private final DyeingBatchRepository dyeingBatchRepo;
    private final CuttingService cuttingService;
    private final SewingService sewingService;
    private final PackingService packingService;

    public DashboardController(YarnStockService yarnService,
                               KnittingService knittingService,
                               DyeingBatchRepository dyeingBatchRepo,
                               CuttingService cuttingService,
                               SewingService sewingService,
                               PackingService packingService) {
        this.yarnService = yarnService;
        this.knittingService = knittingService;
        this.dyeingBatchRepo = dyeingBatchRepo;
        this.cuttingService = cuttingService;
        this.sewingService = sewingService;
        this.packingService = packingService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get end-to-end Yarn-to-Garment conversion pipeline summary")
    public ResponseEntity<ApiResponse<PipelineSummaryDto>> getPipelineSummary() {
        BigDecimal yarnKg = yarnService.getTotalStockWeight();
        BigDecimal fabricKg = knittingService.getTotalProducedFabric();
        BigDecimal dyedKg = dyeingBatchRepo.getTotalDyedFabricWeightKg();
        Integer cutPieces = cuttingService.getTotalCutPieces();
        Integer sewnPcs = sewingService.getTotalPassedGarments();
        Integer rejectedPcs = sewingService.getTotalRejectedGarments();
        Integer packedPcs = packingService.getTotalPackedGarments();

        BigDecimal wastage = BigDecimal.ZERO;
        if (yarnKg != null && yarnKg.compareTo(BigDecimal.ZERO) > 0 && fabricKg != null) {
            wastage = yarnKg.subtract(fabricKg).divide(yarnKg, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }

        PipelineSummaryDto summary = PipelineSummaryDto.builder()
                .totalYarnInwardKg(yarnKg)
                .totalKnittedFabricKg(fabricKg)
                .totalDyedFabricKg(dyedKg)
                .totalCutPieces(cutPieces)
                .totalSewnGarments(sewnPcs)
                .totalRejectedGarments(rejectedPcs)
                .totalPackedGarments(packedPcs)
                .overallWastagePercentage(wastage)
                .build();

        return ResponseEntity.ok(ApiResponse.ok(summary));
    }
}
