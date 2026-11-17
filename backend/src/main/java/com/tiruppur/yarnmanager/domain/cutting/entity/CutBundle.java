package com.tiruppur.yarnmanager.domain.cutting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cut_bundles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CutBundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cutting_order_id", nullable = false)
    @JsonBackReference
    private CuttingOrder cuttingOrder;

    @Column(nullable = false, unique = true, length = 100)
    private String bundleCode;         // QR / Barcode string (e.g. BNDL-PO001-S-01)

    @Column(nullable = false, length = 20)
    private String size;               // S, M, L, XL, XXL

    @Column(nullable = false)
    private Integer quantity;          // Pieces per bundle (e.g. 25 pcs)

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String currentStage = "CUT"; // CUT, PRINTING, EMBROIDERY, SEWING, FINISHED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
