package com.tiruppur.yarnmanager.domain.master.repository;

import com.tiruppur.yarnmanager.domain.master.entity.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
    Optional<ProductMaster> findByProductCode(String productCode);
    List<ProductMaster> findByActiveTrue();
    List<ProductMaster> findByCategoryAndActiveTrue(String category);
    boolean existsByProductCode(String productCode);
}
