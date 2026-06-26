package com.example.countrycity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    public CityRequest(String name, Long population, String zipCode, String description) {
        this.name = name;
        this.population = population;
        this.zipCode = zipCode;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static CityRequestBuilder builder() {
        return new CityRequestBuilder();
    }

    public static class CityRequestBuilder {
        private String name;
        private Long population;
        private String zipCode;
        private String description;

        public CityRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CityRequestBuilder population(Long population) {
            this.population = population;
            return this;
        }

        public CityRequestBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public CityRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CityRequest build() {
            return new CityRequest(name, population, zipCode, description);
        }
    }
}
