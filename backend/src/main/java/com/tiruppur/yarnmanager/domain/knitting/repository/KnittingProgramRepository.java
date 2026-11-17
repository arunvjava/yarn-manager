package com.tiruppur.yarnmanager.domain.knitting.repository;

import com.tiruppur.yarnmanager.domain.knitting.entity.KnittingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnittingProgramRepository extends JpaRepository<KnittingProgram, Long> {
    Optional<KnittingProgram> findByProgramNumber(String programNumber);
}
