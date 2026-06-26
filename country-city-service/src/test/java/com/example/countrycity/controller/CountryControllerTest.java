package com.example.countrycity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.example.countrycity.repository.CountryRepository countryRepository;

    @Autowired
    private com.example.countrycity.repository.CityRepository cityRepository;

    @Autowired
    private com.example.countrycity.config.DataInitializer dataInitializer;

    @org.junit.jupiter.api.BeforeEach
    void resetDatabase() {
        cityRepository.deleteAll();
        countryRepository.deleteAll();
        dataInitializer.run();
    }

    @Test
    void getAllCountries_ShouldReturnPreseededCountries() throws Exception {
        mockMvc.perform(get("/countries")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$[0].name", anyOf(is("India"), is("United States"), is("United Kingdom"), is("Japan"))));
    }

    @Test
    void getCitiesByCountry_ShouldReturnPaginatedCities() throws Exception {
        Long indiaId = countryRepository.findByName("India").orElseThrow().getId();
        mockMvc.perform(get("/countries/" + indiaId + "/cities?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", anyOf(is("Mumbai"), is("Delhi"))))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void getCitiesByCountry_WhenCountryNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/countries/999999/cities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Country not found")));
    }

    @Test
    void createCountry_ShouldReturn201AndCreatedCountry() throws Exception {
        mockMvc.perform(put("/countries/Germany")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Germany")));

        // Check if Germany is added to all countries list
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Germany")));
    }

    @Test
    void createCountry_WhenDuplicateName_ShouldReturn400() throws Exception {
        // India is pre-seeded
        mockMvc.perform(put("/countries/India")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }
}
