package com.tiruppur.yarnmanager.domain.sewing.repository;

import com.tiruppur.yarnmanager.domain.sewing.entity.SewingLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SewingLineRepository extends JpaRepository<SewingLine, Long> {
    Optional<SewingLine> findByLineName(String lineName);
}
