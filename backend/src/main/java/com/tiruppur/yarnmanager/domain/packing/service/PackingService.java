package com.tiruppur.yarnmanager.domain.packing.service;

import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import com.tiruppur.yarnmanager.domain.order.repository.ProductionOrderRepository;
import com.tiruppur.yarnmanager.domain.packing.entity.CartonPacking;
import com.tiruppur.yarnmanager.domain.packing.repository.CartonPackingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PackingService {

    private final CartonPackingRepository cartonRepo;
    private final ProductionOrderRepository orderRepo;

    public PackingService(CartonPackingRepository cartonRepo, ProductionOrderRepository orderRepo) {
        this.cartonRepo = cartonRepo;
        this.orderRepo = orderRepo;
    }

    public List<CartonPacking> getAllCartons() {
        return cartonRepo.findAll();
    }

    @Transactional
    public CartonPacking packCarton(Long orderId,
                                    String packingType,
                                    String sizeBreakdown,
                                    Integer totalGarments,
                                    BigDecimal grossWeightKg,
                                    BigDecimal cbm) {
        ProductionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Production Order not found: " + orderId));

        CartonPacking carton = CartonPacking.builder()
                .order(order)
                .cartonNumber("CTN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .packingType(packingType)
                .sizeBreakdown(sizeBreakdown)
                .totalGarments(totalGarments)
                .grossWeightKg(grossWeightKg)
                .cbm(cbm)
                .isDispatched(false)
                .build();

        return cartonRepo.save(carton);
    }

    @Transactional
    public CartonPacking dispatchCarton(Long cartonId) {
        CartonPacking carton = cartonRepo.findById(cartonId)
                .orElseThrow(() -> new IllegalArgumentException("Carton not found: " + cartonId));
        carton.setIsDispatched(true);
        carton.setDispatchedDate(LocalDate.now());
        return cartonRepo.save(carton);
    }

    public Integer getTotalPackedGarments() {
        return cartonRepo.getTotalPackedGarments();
    }
}
