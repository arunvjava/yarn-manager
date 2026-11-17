package com.tiruppur.yarnmanager.domain.sewing.repository;

import com.tiruppur.yarnmanager.domain.sewing.entity.SewingProductionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SewingProductionLogRepository extends JpaRepository<SewingProductionLog, Long> {

    List<SewingProductionLog> findBySewingLineId(Long lineId);

    @Query("SELECT COALESCE(SUM(l.passedPieces), 0) FROM SewingProductionLog l")
    Integer getTotalPassedPieces();

    @Query("SELECT COALESCE(SUM(l.rejectedPieces), 0) FROM SewingProductionLog l")
    Integer getTotalRejectedPieces();

    @Query("SELECT COALESCE(SUM(l.passedPieces), 0) FROM SewingProductionLog l WHERE l.sewingLine.id = :lineId")
    Integer sumPassedByLineId(@Param("lineId") Long lineId);
}
