package com.tiruppur.yarnmanager.domain.yarn.repository;

import com.tiruppur.yarnmanager.domain.yarn.entity.YarnStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface YarnStockRepository extends JpaRepository<YarnStock, Long> {
    List<YarnStock> findByYarnCount(String yarnCount);
    List<YarnStock> findByMillNameContainingIgnoreCase(String millName);

    @Query("SELECT COALESCE(SUM(y.netWeightKg), 0) FROM YarnStock y")
    BigDecimal getTotalYarnWeightKg();
}
