package com.example.countrycity.dto;

public class CityResponse {
    private Long id;
    private String name;
    private Long countryId;
    private String countryName;
    private Long population;
    private String zipCode;
    private String description;

    public CityResponse() {
    }

    public CityResponse(Long id, String name, Long countryId, String countryName, Long population, String zipCode, String description) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.countryName = countryName;
        this.population = population;
        this.zipCode = zipCode;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public static CityResponseBuilder builder() {
        return new CityResponseBuilder();
    }

    public static class CityResponseBuilder {
        private Long id;
        private String name;
        private Long countryId;
        private String countryName;
        private Long population;
        private String zipCode;
        private String description;

        public CityResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CityResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CityResponseBuilder countryId(Long countryId) {
            this.countryId = countryId;
            return this;
        }

        public CityResponseBuilder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }

        public CityResponseBuilder population(Long population) {
            this.population = population;
            return this;
        }

        public CityResponseBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public CityResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CityResponse build() {
            return new CityResponse(id, name, countryId, countryName, population, zipCode, description);
        }
    }
}
