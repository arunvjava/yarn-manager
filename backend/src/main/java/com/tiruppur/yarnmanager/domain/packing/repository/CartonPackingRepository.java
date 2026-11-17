package com.tiruppur.yarnmanager.domain.packing.repository;

import com.tiruppur.yarnmanager.domain.packing.entity.CartonPacking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartonPackingRepository extends JpaRepository<CartonPacking, Long> {
    Optional<CartonPacking> findByCartonNumber(String cartonNumber);
    List<CartonPacking> findByOrderId(Long orderId);

    @Query("SELECT COALESCE(SUM(c.totalGarments), 0) FROM CartonPacking c")
    Integer getTotalPackedGarments();

    @Query("SELECT COUNT(c) FROM CartonPacking c WHERE c.isDispatched = true")
    Long countDispatchedCartons();
}
