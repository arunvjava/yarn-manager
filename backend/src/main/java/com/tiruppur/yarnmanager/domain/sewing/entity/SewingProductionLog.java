package com.tiruppur.yarnmanager.domain.sewing.entity;

import com.tiruppur.yarnmanager.domain.cutting.entity.CutBundle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sewing_production_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SewingProductionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "line_id", nullable = false)
    private SewingLine sewingLine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bundle_id", nullable = false)
    private CutBundle bundle;

    @Column(nullable = false, length = 100)
    private String operatorName;

    @Column(nullable = false)
    private Integer passedPieces;

    @Column(nullable = false)
    @Builder.Default
    private Integer reworkedPieces = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer rejectedPieces = 0;

    @Builder.Default
    private LocalDateTime loggedAt = LocalDateTime.now();
}
