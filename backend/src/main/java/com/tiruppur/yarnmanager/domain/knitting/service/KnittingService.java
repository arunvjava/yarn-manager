package com.tiruppur.yarnmanager.domain.knitting.service;

import com.tiruppur.yarnmanager.domain.knitting.entity.FabricRoll;
import com.tiruppur.yarnmanager.domain.knitting.entity.KnittingProgram;
import com.tiruppur.yarnmanager.domain.knitting.repository.FabricRollRepository;
import com.tiruppur.yarnmanager.domain.knitting.repository.KnittingProgramRepository;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import com.tiruppur.yarnmanager.domain.order.repository.ProductionOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class KnittingService {

    private final KnittingProgramRepository programRepo;
    private final FabricRollRepository rollRepo;
    private final ProductionOrderRepository orderRepo;

    public KnittingService(KnittingProgramRepository programRepo,
                           FabricRollRepository rollRepo,
                           ProductionOrderRepository orderRepo) {
        this.programRepo = programRepo;
        this.rollRepo = rollRepo;
        this.orderRepo = orderRepo;
    }

    public List<KnittingProgram> getAllPrograms() {
        return programRepo.findAll();
    }

    public KnittingProgram getProgramById(Long id) {
        return programRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Knitting Program not found: " + id));
    }

    @Transactional
    public KnittingProgram createProgram(Long orderId, KnittingProgram program) {
        ProductionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Production Order not found: " + orderId));
        program.setOrder(order);
        if (program.getProgramNumber() == null || program.getProgramNumber().isBlank()) {
            program.setProgramNumber("KNIT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        return programRepo.save(program);
    }

    @Transactional
    public FabricRoll registerFabricRoll(Long programId, BigDecimal rollWeightKg, Integer actualGsm, String inspectedBy) {
        KnittingProgram program = getProgramById(programId);

        FabricRoll roll = FabricRoll.builder()
                .knittingProgram(program)
                .rollNumber("ROLL-TRP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .rollWeightKg(rollWeightKg)
                .actualGsm(actualGsm)
                .qualityGrade(Math.abs(actualGsm - program.getTargetGsm()) <= 10 ? "A" : "B")
                .inspectedBy(inspectedBy != null ? inspectedBy : "Knitting QC")
                .build();

        return rollRepo.save(roll);
    }

    public BigDecimal calculateKnittingWastageLoss(Long programId) {
        KnittingProgram program = getProgramById(programId);
        BigDecimal producedWeight = rollRepo.sumWeightByProgramId(programId);
        BigDecimal allocated = program.getAllocatedYarnKg();

        if (allocated.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal loss = allocated.subtract(producedWeight);
        return loss.divide(allocated, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public List<FabricRoll> getRollsByProgram(Long programId) {
        return rollRepo.findByKnittingProgramId(programId);
    }

    public BigDecimal getTotalProducedFabric() {
        return rollRepo.getTotalProducedFabricWeightKg();
    }
}
