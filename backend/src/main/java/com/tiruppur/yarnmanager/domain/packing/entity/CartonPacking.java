package com.tiruppur.yarnmanager.domain.packing.entity;

import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "carton_packings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartonPacking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private ProductionOrder order;

    @Column(nullable = false, unique = true, length = 100)
    private String cartonNumber;       // e.g. CTN-PO001-001

    @Column(nullable = false, length = 50)
    private String packingType;        // SOLID_SIZE or ASSORTED

    @Column(nullable = false, length = 200)
    private String sizeBreakdown;      // e.g. 'S:10, M:20, L:20, XL:10'

    @Column(nullable = false)
    private Integer totalGarments;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal grossWeightKg;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal cbm;            // Cubic Meters for container / freight

    @Builder.Default
    private Boolean isDispatched = false;

    private LocalDate dispatchedDate;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
