package com.tiruppur.yarnmanager.domain.cutting.repository;

import com.tiruppur.yarnmanager.domain.cutting.entity.CutBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CutBundleRepository extends JpaRepository<CutBundle, Long> {
    Optional<CutBundle> findByBundleCode(String bundleCode);
    List<CutBundle> findByCurrentStage(String currentStage);
    List<CutBundle> findByCuttingOrderId(Long cuttingOrderId);
}
