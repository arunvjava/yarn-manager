package com.tiruppur.yarnmanager.domain.knitting.repository;

import com.tiruppur.yarnmanager.domain.knitting.entity.FabricRoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FabricRollRepository extends JpaRepository<FabricRoll, Long> {
    Optional<FabricRoll> findByRollNumber(String rollNumber);
    List<FabricRoll> findByKnittingProgramId(Long knittingProgramId);

    @Query("SELECT COALESCE(SUM(r.rollWeightKg), 0) FROM FabricRoll r WHERE r.knittingProgram.id = :programId")
    BigDecimal sumWeightByProgramId(@Param("programId") Long programId);

    @Query("SELECT COALESCE(SUM(r.rollWeightKg), 0) FROM FabricRoll r")
    BigDecimal getTotalProducedFabricWeightKg();
}
