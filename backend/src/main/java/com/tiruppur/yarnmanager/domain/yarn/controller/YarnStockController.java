package com.tiruppur.yarnmanager.domain.yarn.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.yarn.entity.YarnStock;
import com.tiruppur.yarnmanager.domain.yarn.service.YarnStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/yarn")
@Tag(name = "Yarn Inventory", description = "Raw yarn stock inward, counts (30s, 34s, Lycra), mills, and issue tracking")
public class YarnStockController {

    private final YarnStockService yarnStockService;

    public YarnStockController(YarnStockService yarnStockService) {
        this.yarnStockService = yarnStockService;
    }

    @GetMapping
    @Operation(summary = "Get all yarn inventory lots")
    public ResponseEntity<ApiResponse<List<YarnStock>>> getAllStock() {
        return ResponseEntity.ok(ApiResponse.ok(yarnStockService.getAllStock()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get yarn stock detail by ID")
    public ResponseEntity<ApiResponse<YarnStock>> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(yarnStockService.getStockById(id)));
    }

    @PostMapping("/inward")
    @Operation(summary = "Record new yarn stock inward receipt")
    public ResponseEntity<ApiResponse<YarnStock>> recordInward(@Valid @RequestBody YarnStock stock) {
        YarnStock created = yarnStockService.recordYarnInward(stock);
        return ResponseEntity.ok(ApiResponse.ok(created, "Yarn inward entry saved successfully"));
    }

    @PostMapping("/{id}/deduct")
    @Operation(summary = "Deduct yarn stock for knitting program allocation")
    public ResponseEntity<ApiResponse<YarnStock>> deductYarn(
            @PathVariable Long id,
            @RequestParam BigDecimal weightKg) {
        YarnStock updated = yarnStockService.deductYarnForKnitting(id, weightKg);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Yarn successfully allocated"));
    }

    @GetMapping("/total-weight")
    @Operation(summary = "Get total available yarn weight in warehouse (kg)")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalStock() {
        return ResponseEntity.ok(ApiResponse.ok(yarnStockService.getTotalStockWeight()));
    }
}
