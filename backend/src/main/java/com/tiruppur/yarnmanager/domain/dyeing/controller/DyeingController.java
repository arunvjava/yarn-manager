package com.tiruppur.yarnmanager.domain.dyeing.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.dyeing.entity.DyeingBatch;
import com.tiruppur.yarnmanager.domain.dyeing.service.DyeingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dyeing")
@Tag(name = "Dyeing & Processing (DC)", description = "Delivery Challans (Outward/Inward), Job Work Units, Shade Matching, and Shrinkage/Loss %")
public class DyeingController {

    private final DyeingService dyeingService;

    public DyeingController(DyeingService dyeingService) {
        this.dyeingService = dyeingService;
    }

    @GetMapping("/batches")
    @Operation(summary = "List all dyeing job work batches")
    public ResponseEntity<ApiResponse<List<DyeingBatch>>> getAllBatches() {
        return ResponseEntity.ok(ApiResponse.ok(dyeingService.getAllBatches()));
    }

    @PostMapping("/outward-dc")
    @Operation(summary = "Generate Outward Delivery Challan to send fabric for dyeing")
    public ResponseEntity<ApiResponse<DyeingBatch>> createOutwardDc(
            @RequestParam Long orderId,
            @RequestParam String dyeingUnitName,
            @RequestParam BigDecimal sentWeightKg,
            @RequestParam String pantoneShade,
            @RequestParam(required = false) String finishType) {
        DyeingBatch batch = dyeingService.createOutwardDc(orderId, dyeingUnitName, sentWeightKg, pantoneShade, finishType);
        return ResponseEntity.ok(ApiResponse.ok(batch, "Outward DC issued for dyeing"));
    }

    @PostMapping("/batches/{batchId}/inward-dc")
    @Operation(summary = "Record Inward DC and reconcile received dyed fabric weight")
    public ResponseEntity<ApiResponse<DyeingBatch>> recordInwardDc(
            @PathVariable Long batchId,
            @RequestParam BigDecimal receivedWeightKg) {
        DyeingBatch batch = dyeingService.recordInwardDc(batchId, receivedWeightKg);
        return ResponseEntity.ok(ApiResponse.ok(batch, "Inward DC received and reconciled"));
    }

    @GetMapping("/batches/{batchId}/loss-percentage")
    @Operation(summary = "Calculate process loss % between Outward and Inward DC")
    public ResponseEntity<ApiResponse<BigDecimal>> getLossPercentage(@PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.ok(dyeingService.calculateProcessingLossPercentage(batchId)));
    }
}
