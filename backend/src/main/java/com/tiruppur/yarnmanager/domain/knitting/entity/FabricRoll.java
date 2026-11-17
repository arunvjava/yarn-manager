package com.tiruppur.yarnmanager.domain.knitting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fabric_rolls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FabricRoll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knitting_program_id", nullable = false)
    @JsonBackReference
    private KnittingProgram knittingProgram;

    @Column(nullable = false, unique = true, length = 100)
    private String rollNumber;         // QR / Barcode ID

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rollWeightKg;

    @Column(nullable = false)
    private Integer actualGsm;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String qualityGrade = "A"; // A, B, REJECT

    @Column(length = 100)
    @Builder.Default
    private String inspectedBy = "Knitting Supervisor";

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
