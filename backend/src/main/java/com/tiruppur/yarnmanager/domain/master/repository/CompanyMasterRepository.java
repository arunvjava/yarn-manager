package com.tiruppur.yarnmanager.domain.master.repository;

import com.tiruppur.yarnmanager.domain.master.entity.CompanyMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyMasterRepository extends JpaRepository<CompanyMaster, Long> {
    Optional<CompanyMaster> findByCompanyCode(String companyCode);
    List<CompanyMaster> findByActiveTrue();
    boolean existsByCompanyCode(String companyCode);
}
