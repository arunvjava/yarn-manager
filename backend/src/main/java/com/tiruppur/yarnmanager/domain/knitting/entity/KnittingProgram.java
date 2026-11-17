package com.tiruppur.yarnmanager.domain.knitting.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "knitting_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnittingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private ProductionOrder order;

    @Column(nullable = false, unique = true, length = 100)
    private String programNumber;      // e.g. KNIT-PRG-001

    @Column(nullable = false)
    private Integer machineDia;        // e.g. 30" (diameter)

    @Column(nullable = false)
    private Integer gauge;             // e.g. 24 GG

    @Column(nullable = false)
    private Integer targetGsm;         // e.g. 180 GSM

    @Column(nullable = false, length = 100)
    private String fabricType;         // Single Jersey, 2x2 Rib, Pique, Fleece

    @Column(precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal loopLength = BigDecimal.valueOf(2.85); // in mm

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedYarnKg;

    @Column(length = 50)
    @Builder.Default
    private String status = "IN_KNITTING"; // PLANNED, IN_KNITTING, COMPLETED

    @OneToMany(mappedBy = "knittingProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<FabricRoll> rolls = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
