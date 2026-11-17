package com.tiruppur.yarnmanager.domain.cutting.service;

import com.tiruppur.yarnmanager.domain.cutting.entity.CutBundle;
import com.tiruppur.yarnmanager.domain.cutting.entity.CuttingOrder;
import com.tiruppur.yarnmanager.domain.cutting.repository.CutBundleRepository;
import com.tiruppur.yarnmanager.domain.cutting.repository.CuttingOrderRepository;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import com.tiruppur.yarnmanager.domain.order.repository.ProductionOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CuttingService {

    private final CuttingOrderRepository cuttingOrderRepo;
    private final CutBundleRepository bundleRepo;
    private final ProductionOrderRepository orderRepo;

    public CuttingService(CuttingOrderRepository cuttingOrderRepo,
                          CutBundleRepository bundleRepo,
                          ProductionOrderRepository orderRepo) {
        this.cuttingOrderRepo = cuttingOrderRepo;
        this.bundleRepo = bundleRepo;
        this.orderRepo = orderRepo;
    }

    public List<CuttingOrder> getAllCuttingOrders() {
        return cuttingOrderRepo.findAll();
    }

    @Transactional
    public CuttingOrder createCuttingOrder(Long orderId,
                                          String layNumber,
                                          Integer totalPlies,
                                          Integer totalCutPieces,
                                          BigDecimal fabricConsumedKg,
                                          BigDecimal cuttingWastageKg) {
        ProductionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Production Order not found: " + orderId));

        CuttingOrder cutting = CuttingOrder.builder()
                .order(order)
                .layNumber(layNumber)
                .totalPlies(totalPlies)
                .totalCutPieces(totalCutPieces)
                .fabricConsumedKg(fabricConsumedKg)
                .cuttingWastageKg(cuttingWastageKg)
                .build();

        return cuttingOrderRepo.save(cutting);
    }

    @Transactional
    public CutBundle generateBundleTicket(Long cuttingOrderId, String size, Integer quantity) {
        CuttingOrder cuttingOrder = cuttingOrderRepo.findById(cuttingOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Cutting order not found: " + cuttingOrderId));

        CutBundle bundle = CutBundle.builder()
                .cuttingOrder(cuttingOrder)
                .bundleCode("BNDL-" + size.toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase())
                .size(size)
                .quantity(quantity)
                .currentStage("CUT")
                .build();

        return bundleRepo.save(bundle);
    }

    @Transactional
    public CutBundle updateBundleStage(String bundleCode, String newStage) {
        CutBundle bundle = bundleRepo.findByBundleCode(bundleCode)
                .orElseThrow(() -> new IllegalArgumentException("Bundle not found for QR code: " + bundleCode));
        bundle.setCurrentStage(newStage.toUpperCase());
        return bundleRepo.save(bundle);
    }

    public List<CutBundle> getBundlesByCuttingOrder(Long cuttingOrderId) {
        return bundleRepo.findByCuttingOrderId(cuttingOrderId);
    }

    public Integer getTotalCutPieces() {
        return cuttingOrderRepo.getTotalCutPieces();
    }
}
