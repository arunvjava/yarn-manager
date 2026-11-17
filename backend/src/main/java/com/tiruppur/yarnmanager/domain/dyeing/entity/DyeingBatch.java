package com.tiruppur.yarnmanager.domain.dyeing.entity;

import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dyeing_batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DyeingBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private ProductionOrder order;

    @Column(nullable = false, unique = true, length = 100)
    private String batchNumber;        // e.g. DYE-BATCH-2026-88

    @Column(nullable = false, length = 150)
    private String dyeingUnitName;     // Job worker unit (e.g. SIPCOT Perundurai)

    @Column(nullable = false, length = 100)
    private String outwardDcNumber;    // Outward Delivery Challan Number

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal sentWeightKg;

    @Column(precision = 12, scale = 2)
    private BigDecimal receivedWeightKg; // Reconciled inward DC weight

    @Column(nullable = false, length = 100)
    private String pantoneShade;       // e.g. Navy Blue 19-4024 TPX

    @Column(length = 100)
    @Builder.Default
    private String finishType = "Bio-Polish Silicon Finish";

    @Column(length = 50)
    @Builder.Default
    private String status = "SENT_TO_DYEING"; // SENT_TO_DYEING, IN_PROCESS, RECEIVED, QA_APPROVED

    @Builder.Default
    private LocalDate sentDate = LocalDate.now();

    private LocalDate receivedDate;
}
