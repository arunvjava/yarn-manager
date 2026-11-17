package com.tiruppur.yarnmanager.domain.dyeing.service;

import com.tiruppur.yarnmanager.domain.dyeing.entity.DyeingBatch;
import com.tiruppur.yarnmanager.domain.dyeing.repository.DyeingBatchRepository;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import com.tiruppur.yarnmanager.domain.order.repository.ProductionOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DyeingService {

    private final DyeingBatchRepository batchRepo;
    private final ProductionOrderRepository orderRepo;

    public DyeingService(DyeingBatchRepository batchRepo, ProductionOrderRepository orderRepo) {
        this.batchRepo = batchRepo;
        this.orderRepo = orderRepo;
    }

    public List<DyeingBatch> getAllBatches() {
        return batchRepo.findAll();
    }

    public DyeingBatch getBatchById(Long id) {
        return batchRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dyeing Batch not found: " + id));
    }

    @Transactional
    public DyeingBatch createOutwardDc(Long orderId,
                                       String dyeingUnitName,
                                       BigDecimal sentWeightKg,
                                       String pantoneShade,
                                       String finishType) {
        ProductionOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Production Order not found: " + orderId));

        DyeingBatch batch = DyeingBatch.builder()
                .order(order)
                .batchNumber("DYE-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .dyeingUnitName(dyeingUnitName)
                .outwardDcNumber("DC-OUT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .sentWeightKg(sentWeightKg)
                .pantoneShade(pantoneShade)
                .finishType(finishType != null ? finishType : "Bio-Wash Silicon Finish")
                .status("SENT_TO_DYEING")
                .sentDate(LocalDate.now())
                .build();

        return batchRepo.save(batch);
    }

    @Transactional
    public DyeingBatch recordInwardDc(Long batchId, BigDecimal receivedWeightKg) {
        DyeingBatch batch = getBatchById(batchId);
        batch.setReceivedWeightKg(receivedWeightKg);
        batch.setReceivedDate(LocalDate.now());
        batch.setStatus("RECEIVED");
        return batchRepo.save(batch);
    }

    public BigDecimal calculateProcessingLossPercentage(Long batchId) {
        DyeingBatch batch = getBatchById(batchId);
        if (batch.getReceivedWeightKg() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal sent = batch.getSentWeightKg();
        BigDecimal received = batch.getReceivedWeightKg();
        BigDecimal loss = sent.subtract(received);
        return loss.divide(sent, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }
}
