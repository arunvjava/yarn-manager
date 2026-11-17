package com.tiruppur.yarnmanager.domain.sewing.service;

import com.tiruppur.yarnmanager.domain.cutting.entity.CutBundle;
import com.tiruppur.yarnmanager.domain.cutting.repository.CutBundleRepository;
import com.tiruppur.yarnmanager.domain.sewing.entity.SewingLine;
import com.tiruppur.yarnmanager.domain.sewing.entity.SewingProductionLog;
import com.tiruppur.yarnmanager.domain.sewing.repository.SewingLineRepository;
import com.tiruppur.yarnmanager.domain.sewing.repository.SewingProductionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SewingService {

    private final SewingLineRepository lineRepo;
    private final SewingProductionLogRepository logRepo;
    private final CutBundleRepository bundleRepo;

    public SewingService(SewingLineRepository lineRepo,
                         SewingProductionLogRepository logRepo,
                         CutBundleRepository bundleRepo) {
        this.lineRepo = lineRepo;
        this.logRepo = logRepo;
        this.bundleRepo = bundleRepo;
    }

    public List<SewingLine> getAllLines() {
        return lineRepo.findAll();
    }

    @Transactional
    public SewingProductionLog logSewingOutput(Long lineId,
                                              String bundleCode,
                                              String operatorName,
                                              Integer passed,
                                              Integer reworked,
                                              Integer rejected) {
        SewingLine line = lineRepo.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("Sewing Line not found: " + lineId));
        CutBundle bundle = bundleRepo.findByBundleCode(bundleCode)
                .orElseThrow(() -> new IllegalArgumentException("Bundle not found: " + bundleCode));

        SewingProductionLog log = SewingProductionLog.builder()
                .sewingLine(line)
                .bundle(bundle)
                .operatorName(operatorName)
                .passedPieces(passed)
                .reworkedPieces(reworked != null ? reworked : 0)
                .rejectedPieces(rejected != null ? rejected : 0)
                .build();

        bundle.setCurrentStage("SEWING");
        bundleRepo.save(bundle);

        return logRepo.save(log);
    }

    public List<SewingProductionLog> getLogsForLine(Long lineId) {
        return logRepo.findBySewingLineId(lineId);
    }

    public Integer getTotalPassedGarments() {
        return logRepo.getTotalPassedPieces();
    }

    public Integer getTotalRejectedGarments() {
        return logRepo.getTotalRejectedPieces();
    }
}
