package com.example.countrycity.controller;

import com.example.countrycity.dto.CityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void getCityById_ShouldReturnPreseededCity() throws Exception {
        com.example.countrycity.entity.City mumbai = cityRepository.findAll().stream()
                .filter(c -> c.getName().equals("Mumbai"))
                .findFirst().orElseThrow();
        Long mumbaiId = mumbai.getId();

        mockMvc.perform(get("/cities/" + mumbaiId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mumbaiId.intValue())))
                .andExpect(jsonPath("$.name", is("Mumbai")))
                .andExpect(jsonPath("$.population", is(20000000)))
                .andExpect(jsonPath("$.countryName", is("India")))
                .andExpect(jsonPath("$.zipCode", is("400001")))
                .andExpect(jsonPath("$.description", containsString("Financial capital")));
    }

    @Test
    void getCityById_WhenNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/cities/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("City not found")));
    }

    @Test
    void createCityByName_ShouldReturn201AndCreatedCity() throws Exception {
        Long indiaId = countryRepository.findByName("India").orElseThrow().getId();
        mockMvc.perform(put("/countries/" + indiaId + "/Bangalore")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bangalore")))
                .andExpect(jsonPath("$.countryId", is(indiaId.intValue())))
                .andExpect(jsonPath("$.population", is(0))); // default
    }

    @Test
    void createCityWithDetails_ShouldReturn201AndCreatedCity() throws Exception {
        Long indiaId = countryRepository.findByName("India").orElseThrow().getId();
        CityRequest request = CityRequest.builder()
                .name("Chennai")
                .population(7000000L)
                .zipCode("600001")
                .description("Gateway to South India")
                .build();

        mockMvc.perform(post("/countries/" + indiaId + "/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Chennai")))
                .andExpect(jsonPath("$.population", is(7000000)))
                .andExpect(jsonPath("$.zipCode", is("600001")))
                .andExpect(jsonPath("$.description", is("Gateway to South India")));
    }

    @Test
    void createCityWithDetails_WhenValidationFails_ShouldReturn400() throws Exception {
        Long indiaId = countryRepository.findByName("India").orElseThrow().getId();
        // Negative population, empty name
        CityRequest request = CityRequest.builder()
                .name("")
                .population(-500L)
                .zipCode("123456789012345678901") // 21 chars, exceeds size limit 20
                .description("Invalid data test")
                .build();

        mockMvc.perform(post("/countries/" + indiaId + "/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.name", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.population", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.zipCode", notNullValue()));
    }
}
