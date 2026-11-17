package com.tiruppur.yarnmanager.domain.yarn.service;

import com.tiruppur.yarnmanager.domain.yarn.entity.YarnStock;
import com.tiruppur.yarnmanager.domain.yarn.repository.YarnStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class YarnStockService {

    private final YarnStockRepository yarnStockRepository;

    public YarnStockService(YarnStockRepository yarnStockRepository) {
        this.yarnStockRepository = yarnStockRepository;
    }

    public List<YarnStock> getAllStock() {
        return yarnStockRepository.findAll();
    }

    public YarnStock getStockById(Long id) {
        return yarnStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Yarn stock item not found: " + id));
    }

    @Transactional
    public YarnStock recordYarnInward(YarnStock stock) {
        return yarnStockRepository.save(stock);
    }

    @Transactional
    public YarnStock deductYarnForKnitting(Long yarnStockId, BigDecimal weightToDeduct) {
        YarnStock stock = getStockById(yarnStockId);
        if (stock.getNetWeightKg().compareTo(weightToDeduct) < 0) {
            throw new IllegalArgumentException("Insufficient yarn stock! Available: " + stock.getNetWeightKg() + " kg, Requested: " + weightToDeduct + " kg");
        }
        stock.setNetWeightKg(stock.getNetWeightKg().subtract(weightToDeduct));
        return yarnStockRepository.save(stock);
    }

    public BigDecimal getTotalStockWeight() {
        return yarnStockRepository.getTotalYarnWeightKg();
    }
}
