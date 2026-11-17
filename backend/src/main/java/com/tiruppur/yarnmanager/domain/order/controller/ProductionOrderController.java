package com.tiruppur.yarnmanager.domain.order.controller;

import com.tiruppur.yarnmanager.common.ApiResponse;
import com.tiruppur.yarnmanager.domain.order.entity.Buyer;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import com.tiruppur.yarnmanager.domain.order.repository.BuyerRepository;
import com.tiruppur.yarnmanager.domain.order.repository.ProductionOrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Production Orders & Buyers", description = "Buyer styles, purchase orders and production lots")
public class ProductionOrderController {

    private final ProductionOrderRepository orderRepository;
    private final BuyerRepository buyerRepository;

    public ProductionOrderController(ProductionOrderRepository orderRepository, BuyerRepository buyerRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
    }

    @GetMapping
    @Operation(summary = "List all production orders")
    public ResponseEntity<ApiResponse<List<ProductionOrder>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.ok(orderRepository.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<ProductionOrder>> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(ApiResponse.ok(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new production order")
    public ResponseEntity<ApiResponse<ProductionOrder>> createOrder(@Valid @RequestBody ProductionOrder order) {
        if (order.getBuyer() != null && order.getBuyer().getId() != null) {
            Buyer buyer = buyerRepository.findById(order.getBuyer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Buyer not found: " + order.getBuyer().getId()));
            order.setBuyer(buyer);
        }
        ProductionOrder saved = orderRepository.save(order);
        return ResponseEntity.ok(ApiResponse.ok(saved, "Production order created successfully"));
    }

    @GetMapping("/buyers")
    @Operation(summary = "List all registered buyers")
    public ResponseEntity<ApiResponse<List<Buyer>>> getAllBuyers() {
        return ResponseEntity.ok(ApiResponse.ok(buyerRepository.findAll()));
    }

    @PostMapping("/buyers")
    @Operation(summary = "Register a new buyer")
    public ResponseEntity<ApiResponse<Buyer>> createBuyer(@Valid @RequestBody Buyer buyer) {
        return ResponseEntity.ok(ApiResponse.ok(buyerRepository.save(buyer), "Buyer registered successfully"));
    }
}
