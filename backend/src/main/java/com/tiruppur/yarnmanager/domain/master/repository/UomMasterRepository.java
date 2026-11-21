package com.tiruppur.yarnmanager.domain.master.repository;

import com.tiruppur.yarnmanager.domain.master.entity.UomMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UomMasterRepository extends JpaRepository<UomMaster, Long> {
    Optional<UomMaster> findByUomCode(String uomCode);
    List<UomMaster> findByActiveTrue();
    List<UomMaster> findByCategoryAndActiveTrue(String category);
    boolean existsByUomCode(String uomCode);
}
