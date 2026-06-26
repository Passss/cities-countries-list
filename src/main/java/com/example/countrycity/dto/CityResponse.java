package com.example.countrycity.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class CityResponse {
    private Long id;
    private String name;
    private Long countryId;
    private String countryName;
    private Long population;
    private String zipCode;
    private String description;
}
