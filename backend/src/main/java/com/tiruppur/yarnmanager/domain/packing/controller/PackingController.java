package com.tiruppur.yarnmanager.domain.packing.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.packing.entity.CartonPacking;
import com.tiruppur.yarnmanager.domain.packing.service.PackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/packing")
@Tag(name = "Finishing & Carton Packaging", description = "Carton box packing lists, solid/assorted ratio, CBM volume, and export dispatch")
public class PackingController {

    private final PackingService packingService;

    public PackingController(PackingService packingService) {
        this.packingService = packingService;
    }

    @GetMapping("/cartons")
    @Operation(summary = "List all packed cartons")
    public ResponseEntity<ApiResponse<List<CartonPacking>>> getAllCartons() {
        return ResponseEntity.ok(ApiResponse.ok(packingService.getAllCartons()));
    }

    @PostMapping("/cartons")
    @Operation(summary = "Pack garments into a carton box")
    public ResponseEntity<ApiResponse<CartonPacking>> packCarton(
            @RequestParam Long orderId,
            @RequestParam String packingType,
            @RequestParam String sizeBreakdown,
            @RequestParam Integer totalGarments,
            @RequestParam BigDecimal grossWeightKg,
            @RequestParam BigDecimal cbm) {
        CartonPacking carton = packingService.packCarton(orderId, packingType, sizeBreakdown, totalGarments, grossWeightKg, cbm);
        return ResponseEntity.ok(ApiResponse.ok(carton, "Carton packed and labeled"));
    }

    @PutMapping("/cartons/{cartonId}/dispatch")
    @Operation(summary = "Dispatch carton for container / shipping shipment")
    public ResponseEntity<ApiResponse<CartonPacking>> dispatchCarton(@PathVariable Long cartonId) {
        CartonPacking carton = packingService.dispatchCarton(cartonId);
        return ResponseEntity.ok(ApiResponse.ok(carton, "Carton dispatched for shipment"));
    }
}
