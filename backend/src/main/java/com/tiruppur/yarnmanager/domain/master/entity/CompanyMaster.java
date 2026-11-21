package com.tiruppur.yarnmanager.domain.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String companyName; // Legal Name, e.g. "Tiruppur TexFlow Garments Pvt Ltd"

    @Column(nullable = false, unique = true, length = 50)
    private String companyCode; // e.g. "TTG-UNIT-01"

    @Column(length = 100)
    private String unitOrDivision; // "Unit 1 - Knitting & Garments, Angeripalayam"

    @Column(length = 50, nullable = false)
    private String gstin; // GSTIN e.g. "33AAACT1234F1Z5"

    @Column(length = 20)
    private String pan; // Permanent Account Number

    @Column(length = 50)
    private String iecCode; // Import Export Code for customs

    @Column(length = 500, nullable = false)
    private String registeredAddress;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 150)
    private String contactEmail;

    @Column(length = 50)
    private String contactPhone;

    @Column(length = 150)
    private String bankName;

    @Column(length = 50)
    private String bankAccountNumber;

    @Column(length = 20)
    private String bankIfsc;

    @Builder.Default
    private boolean isPrimary = true; // Primary headquarters / invoicing entity

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
