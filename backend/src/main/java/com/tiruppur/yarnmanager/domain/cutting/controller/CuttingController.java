package com.tiruppur.yarnmanager.domain.cutting.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.cutting.entity.CutBundle;
import com.tiruppur.yarnmanager.domain.cutting.entity.CuttingOrder;
import com.tiruppur.yarnmanager.domain.cutting.service.CuttingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cutting")
@Tag(name = "Cutting & Bundle QR Tickets", description = "Lay planning, marker spreading, bundle generation, and work-in-progress stage tracking")
public class CuttingController {

    private final CuttingService cuttingService;

    public CuttingController(CuttingService cuttingService) {
        this.cuttingService = cuttingService;
    }

    @GetMapping("/orders")
    @Operation(summary = "List all cutting lay orders")
    public ResponseEntity<ApiResponse<List<CuttingOrder>>> getAllCuttingOrders() {
        return ResponseEntity.ok(ApiResponse.ok(cuttingService.getAllCuttingOrders()));
    }

    @PostMapping("/orders")
    @Operation(summary = "Create cutting lay order")
    public ResponseEntity<ApiResponse<CuttingOrder>> createCuttingOrder(
            @RequestParam Long orderId,
            @RequestParam String layNumber,
            @RequestParam Integer totalPlies,
            @RequestParam Integer totalCutPieces,
            @RequestParam BigDecimal fabricConsumedKg,
            @RequestParam BigDecimal cuttingWastageKg) {
        CuttingOrder order = cuttingService.createCuttingOrder(orderId, layNumber, totalPlies, totalCutPieces, fabricConsumedKg, cuttingWastageKg);
        return ResponseEntity.ok(ApiResponse.ok(order, "Cutting lay registered"));
    }

    @PostMapping("/orders/{cuttingOrderId}/bundles")
    @Operation(summary = "Generate bundle QR ticket for sewing line issue")
    public ResponseEntity<ApiResponse<CutBundle>> generateBundle(
            @PathVariable Long cuttingOrderId,
            @RequestParam String size,
            @RequestParam Integer quantity) {
        CutBundle bundle = cuttingService.generateBundleTicket(cuttingOrderId, size, quantity);
        return ResponseEntity.ok(ApiResponse.ok(bundle, "Bundle ticket generated"));
    }

    @PutMapping("/bundles/stage")
    @Operation(summary = "Scan QR and advance bundle to next stage (PRINTING, EMBROIDERY, SEWING, FINISHED)")
    public ResponseEntity<ApiResponse<CutBundle>> updateBundleStage(
            @RequestParam String bundleCode,
            @RequestParam String newStage) {
        CutBundle bundle = cuttingService.updateBundleStage(bundleCode, newStage);
        return ResponseEntity.ok(ApiResponse.ok(bundle, "Bundle stage updated to " + newStage));
    }
}
