package com.tiruppur.yarnmanager.domain.sewing.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.sewing.entity.SewingLine;
import com.tiruppur.yarnmanager.domain.sewing.entity.SewingProductionLog;
import com.tiruppur.yarnmanager.domain.sewing.service.SewingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sewing")
@Tag(name = "Sewing Assembly Lines", description = "Line balancing, hourly output, operator piece rate, pass/rework/defect tracking")
public class SewingController {

    private final SewingService sewingService;

    public SewingController(SewingService sewingService) {
        this.sewingService = sewingService;
    }

    @GetMapping("/lines")
    @Operation(summary = "List all sewing assembly lines")
    public ResponseEntity<ApiResponse<List<SewingLine>>> getAllLines() {
        return ResponseEntity.ok(ApiResponse.ok(sewingService.getAllLines()));
    }

    @PostMapping("/logs")
    @Operation(summary = "Record hourly production output for a sewing line")
    public ResponseEntity<ApiResponse<SewingProductionLog>> logProduction(
            @RequestParam Long lineId,
            @RequestParam String bundleCode,
            @RequestParam String operatorName,
            @RequestParam Integer passed,
            @RequestParam(required = false, defaultValue = "0") Integer reworked,
            @RequestParam(required = false, defaultValue = "0") Integer rejected) {
        SewingProductionLog log = sewingService.logSewingOutput(lineId, bundleCode, operatorName, passed, reworked, rejected);
        return ResponseEntity.ok(ApiResponse.ok(log, "Sewing output recorded"));
    }

    @GetMapping("/lines/{lineId}/logs")
    @Operation(summary = "Get production logs by sewing line")
    public ResponseEntity<ApiResponse<List<SewingProductionLog>>> getLineLogs(@PathVariable Long lineId) {
        return ResponseEntity.ok(ApiResponse.ok(sewingService.getLogsForLine(lineId)));
    }
}
