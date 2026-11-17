package com.tiruppur.yarnmanager.domain.cutting.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cutting_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuttingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private ProductionOrder order;

    @Column(nullable = false, length = 100)
    private String layNumber;          // e.g. LAY-01-NAVY-MENS

    @Column(nullable = false)
    private Integer totalPlies;        // number of fabric plies spread on table

    @Column(nullable = false)
    private Integer totalCutPieces;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal fabricConsumedKg;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cuttingWastageKg; // end bits & cut waste

    @OneToMany(mappedBy = "cuttingOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<CutBundle> bundles = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
