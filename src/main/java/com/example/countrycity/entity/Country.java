package com.example.countrycity.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "countries",
    indexes = {
        @Index(name = "idx_country_name", columnList = "name")
    }
)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<City> cities = new ArrayList<>();

    public Country() {
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    // Helper static builder to simulate builder pattern if needed
    public static CountryBuilder builder() {
        return new CountryBuilder();
    }

    public static class CountryBuilder {
        private Long id;
        private String name;
        private List<City> cities = new ArrayList<>();

        public CountryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CountryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CountryBuilder cities(List<City> cities) {
            this.cities = cities;
            return this;
        }

        public Country build() {
            Country country = new Country(this.id, this.name);
            country.setCities(this.cities);
            return country;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id) && Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
