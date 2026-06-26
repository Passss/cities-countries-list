package com.example.countrycity.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class CountryResponse {
    private Long id;
    private String name;
}
