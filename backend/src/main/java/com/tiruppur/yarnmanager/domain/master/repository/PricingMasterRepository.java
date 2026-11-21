package com.tiruppur.yarnmanager.domain.master.repository;

import com.tiruppur.yarnmanager.domain.master.entity.PricingMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PricingMasterRepository extends JpaRepository<PricingMaster, Long> {
    Optional<PricingMaster> findByPriceCode(String priceCode);
    List<PricingMaster> findByActiveTrue();
    List<PricingMaster> findByProcessTypeAndActiveTrue(String processType);
    boolean existsByPriceCode(String priceCode);

    @Query("SELECT p FROM PricingMaster p WHERE p.processType = :processType " +
           "AND p.categoryOrSpec = :category " +
           "AND p.active = true " +
           "AND (p.effectiveFrom <= :date AND (p.effectiveTo IS NULL OR p.effectiveTo >= :date)) " +
           "AND (:buyerId IS NULL OR p.buyer.id = :buyerId OR p.buyer IS NULL) " +
           "ORDER BY p.buyer.id DESC NULLS LAST")
    List<PricingMaster> findMatchingRates(@Param("processType") String processType,
                                          @Param("category") String category,
                                          @Param("date") LocalDate date,
                                          @Param("buyerId") Long buyerId);
}
