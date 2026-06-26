package com.example.countrycity.entity;

import jakarta.persistence.*;
import java.util.Objects;

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

    public City() {
    }

    public City(Long id, String name, Country country, Long population, String zipCode, String description) {
        this.id = id;
        this.name = name;
        this.country = country;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public static CityBuilder builder() {
        return new CityBuilder();
    }

    public static class CityBuilder {
        private Long id;
        private String name;
        private Country country;
        private Long population = 0L;
        private String zipCode;
        private String description;

        public CityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CityBuilder country(Country country) {
            this.country = country;
            return this;
        }

        public CityBuilder population(Long population) {
            this.population = population;
            return this;
        }

        public CityBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public CityBuilder description(String description) {
            this.description = description;
            return this;
        }

        public City build() {
            return new City(id, name, country, population, zipCode, description);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id) &&
                Objects.equals(name, city.name) &&
                Objects.equals(population, city.population) &&
                Objects.equals(zipCode, city.zipCode) &&
                Objects.equals(description, city.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, population, zipCode, description);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", zipCode='" + zipCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
