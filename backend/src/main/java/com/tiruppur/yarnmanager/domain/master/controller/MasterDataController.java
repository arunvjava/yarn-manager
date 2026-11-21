package com.tiruppur.yarnmanager.domain.master.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.master.entity.PricingMaster;
import com.tiruppur.yarnmanager.domain.master.entity.UomMaster;
import com.tiruppur.yarnmanager.domain.master.service.MasterDataService;
import com.tiruppur.yarnmanager.domain.order.entity.Buyer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/master")
@Tag(name = "Master Data Management", description = "Dynamic setup for Units of Measure (UOM), Price Lists & Rate Cards, and Comprehensive Customer/Buyer Profiles")
@SecurityRequirement(name = "Bearer Authentication")
public class MasterDataController {

    private final MasterDataService masterDataService;

    public MasterDataController(MasterDataService masterDataService) {
        this.masterDataService = masterDataService;
    }

    // ==========================================
    // 1. UOM Endpoints
    // ==========================================
    @GetMapping("/uom")
    @Operation(summary = "List Units of Measure (KG, BAG, PCS, MTR, CONE, ROLL, etc.)")
    public ResponseEntity<ApiResponse<List<UomMaster>>> getUoms(
            @RequestParam(required = false, defaultValue = "false") Boolean activeOnly) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.getAllUoms(activeOnly)));
    }

    @GetMapping("/uom/{code}")
    @Operation(summary = "Get UOM details by code")
    public ResponseEntity<ApiResponse<UomMaster>> getUomByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.getUomByCode(code)));
    }

    @PostMapping("/uom")
    @Operation(summary = "Create or update Unit of Measure")
    public ResponseEntity<ApiResponse<UomMaster>> createUom(@Valid @RequestBody UomMaster uom) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.saveUom(uom), "UOM saved successfully"));
    }

    @GetMapping("/uom/convert")
    @Operation(summary = "Convert quantity between units in the same category (e.g. 50 Bags -> Kg)")
    public ResponseEntity<ApiResponse<BigDecimal>> convertUom(
            @RequestParam BigDecimal quantity,
            @RequestParam String from,
            @RequestParam String to) {
        BigDecimal result = masterDataService.convertUom(quantity, from, to);
        return ResponseEntity.ok(ApiResponse.ok(result, quantity + " " + from + " = " + result + " " + to));
    }

    // ==========================================
    // 2. Customer / Buyer Master Endpoints
    // ==========================================
    @GetMapping("/customers")
    @Operation(summary = "List all customer accounts with commercial terms, currency, port, and tolerance")
    public ResponseEntity<ApiResponse<List<Buyer>>> getCustomers(
            @RequestParam(required = false, defaultValue = "false") Boolean activeOnly) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.getAllCustomers(activeOnly)));
    }

    @GetMapping("/customers/{id}")
    @Operation(summary = "Get customer details by ID")
    public ResponseEntity<ApiResponse<Buyer>> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.getCustomerById(id)));
    }

    @PostMapping("/customers")
    @Operation(summary = "Create new customer master account")
    public ResponseEntity<ApiResponse<Buyer>> createCustomer(@Valid @RequestBody Buyer customer) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.saveCustomer(customer), "Customer created successfully"));
    }

    @PutMapping("/customers/{id}")
    @Operation(summary = "Update customer master profile")
    public ResponseEntity<ApiResponse<Buyer>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody Buyer customer) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.updateCustomer(id, customer), "Customer updated successfully"));
    }

    // ==========================================
    // 3. Pricing Master Endpoints
    // ==========================================
    @GetMapping("/pricing")
    @Operation(summary = "List rate cards (Knitting, Dyeing, Yarn, Sewing CM, Printing)")
    public ResponseEntity<ApiResponse<List<PricingMaster>>> getPricing(
            @RequestParam(required = false, defaultValue = "false") Boolean activeOnly,
            @RequestParam(required = false) String processType) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.getAllPrices(activeOnly, processType)));
    }

    @GetMapping("/pricing/{code}")
    @Operation(summary = "Get price rule by code")
    public ResponseEntity<ApiResponse<PricingMaster>> getPriceByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.getPriceByCode(code)));
    }

    @PostMapping("/pricing")
    @Operation(summary = "Create or update rate card entry")
    public ResponseEntity<ApiResponse<PricingMaster>> createPrice(@Valid @RequestBody PricingMaster price) {
        return ResponseEntity.ok(ApiResponse.ok(masterDataService.savePrice(price), "Pricing rule saved successfully"));
    }

    @GetMapping("/pricing/resolve")
    @Operation(summary = "Resolve effective job-work or material rate for a process and spec")
    public ResponseEntity<ApiResponse<PricingMaster>> resolveRate(
            @RequestParam String processType,
            @RequestParam String spec,
            @RequestParam(required = false) Long buyerId) {
        PricingMaster rate = masterDataService.resolveEffectiveRate(processType, spec, buyerId);
        return ResponseEntity.ok(ApiResponse.ok(rate, "Effective rate resolved successfully"));
    }
}
