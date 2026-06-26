package com.example.countrycity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(
    name = "cities",
    indexes = {
        @Index(name = "idx_city_name", columnList = "name"),
        @Index(name = "idx_city_country_id", columnList = "country_id")
    }
)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false)
    private Long population;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(length = 500)
    private String description;
}
