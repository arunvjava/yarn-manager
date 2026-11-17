package com.tiruppur.yarnmanager.domain.dyeing.repository;

import com.tiruppur.yarnmanager.domain.dyeing.entity.DyeingBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DyeingBatchRepository extends JpaRepository<DyeingBatch, Long> {
    Optional<DyeingBatch> findByBatchNumber(String batchNumber);
    Optional<DyeingBatch> findByOutwardDcNumber(String outwardDcNumber);
    List<DyeingBatch> findByStatus(String status);

    @Query("SELECT COALESCE(SUM(d.receivedWeightKg), 0) FROM DyeingBatch d WHERE d.receivedWeightKg IS NOT NULL")
    BigDecimal getTotalDyedFabricWeightKg();
}
