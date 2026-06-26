package com.example.countrycity.dto;

public class CountryResponse {
    private Long id;
    private String name;

    public CountryResponse() {
    }

    public CountryResponse(Long id, String name) {
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

    public static CountryResponseBuilder builder() {
        return new CountryResponseBuilder();
    }

    public static class CountryResponseBuilder {
        private Long id;
        private String name;

        public CountryResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CountryResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CountryResponse build() {
            return new CountryResponse(id, name);
        }
    }
}
