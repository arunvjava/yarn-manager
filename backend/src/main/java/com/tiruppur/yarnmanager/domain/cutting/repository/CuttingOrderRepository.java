package com.tiruppur.yarnmanager.domain.cutting.repository;

import com.tiruppur.yarnmanager.domain.cutting.entity.CuttingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuttingOrderRepository extends JpaRepository<CuttingOrder, Long> {

    @Query("SELECT COALESCE(SUM(c.totalCutPieces), 0) FROM CuttingOrder c")
    Integer getTotalCutPieces();
}
