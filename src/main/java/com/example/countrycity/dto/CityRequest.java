package com.example.countrycity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class CityRequest {

    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Population is required")
    @Min(value = 0, message = "Population must be non-negative")
    private Long population;

    @Size(max = 20, message = "Zip code cannot exceed 20 characters")
    private String zipCode;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    public CityRequest() {
    }
}
