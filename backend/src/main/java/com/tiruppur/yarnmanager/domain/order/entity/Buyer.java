package com.tiruppur.yarnmanager.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String buyerCode;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 150)
    private String contactEmail;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
