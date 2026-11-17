package com.tiruppur.yarnmanager.domain.knitting.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.knitting.entity.FabricRoll;
import com.tiruppur.yarnmanager.domain.knitting.entity.KnittingProgram;
import com.tiruppur.yarnmanager.domain.knitting.service.KnittingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/knitting")
@Tag(name = "Knitting & Fabric Rolls", description = "Circular knitting machines, Dia/Gauge programs, fabric rolls, and wastage %")
public class KnittingController {

    private final KnittingService knittingService;

    public KnittingController(KnittingService knittingService) {
        this.knittingService = knittingService;
    }

    @GetMapping("/programs")
    @Operation(summary = "List all circular knitting programs")
    public ResponseEntity<ApiResponse<List<KnittingProgram>>> getAllPrograms() {
        return ResponseEntity.ok(ApiResponse.ok(knittingService.getAllPrograms()));
    }

    @PostMapping("/programs")
    @Operation(summary = "Create knitting program for an order")
    public ResponseEntity<ApiResponse<KnittingProgram>> createProgram(
            @RequestParam Long orderId,
            @RequestBody KnittingProgram program) {
        return ResponseEntity.ok(ApiResponse.ok(knittingService.createProgram(orderId, program), "Program created"));
    }

    @PostMapping("/programs/{programId}/rolls")
    @Operation(summary = "Register produced fabric roll with barcode and GSM test")
    public ResponseEntity<ApiResponse<FabricRoll>> addRoll(
            @PathVariable Long programId,
            @RequestParam BigDecimal weightKg,
            @RequestParam Integer actualGsm,
            @RequestParam(required = false) String inspectedBy) {
        FabricRoll roll = knittingService.registerFabricRoll(programId, weightKg, actualGsm, inspectedBy);
        return ResponseEntity.ok(ApiResponse.ok(roll, "Fabric roll registered with barcode"));
    }

    @GetMapping("/programs/{programId}/rolls")
    @Operation(summary = "Get all rolls produced under knitting program")
    public ResponseEntity<ApiResponse<List<FabricRoll>>> getRolls(@PathVariable Long programId) {
        return ResponseEntity.ok(ApiResponse.ok(knittingService.getRollsByProgram(programId)));
    }

    @GetMapping("/programs/{programId}/loss-percentage")
    @Operation(summary = "Calculate yarn-to-fabric knitting loss percentage")
    public ResponseEntity<ApiResponse<BigDecimal>> getLossPercentage(@PathVariable Long programId) {
        return ResponseEntity.ok(ApiResponse.ok(knittingService.calculateKnittingWastageLoss(programId)));
    }
}
