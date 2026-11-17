package com.tiruppur.yarnmanager.domain.sewing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sewing_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SewingLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String lineName;           // e.g. Line 1 - Overlock/Flatlock

    @Column(nullable = false, length = 100)
    private String supervisorName;

    @Column(nullable = false)
    @Builder.Default
    private Integer hourlyTargetPieces = 120;
}
