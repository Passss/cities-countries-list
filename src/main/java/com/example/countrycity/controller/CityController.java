package com.example.countrycity.controller;

import com.example.countrycity.dto.CityRequest;
import com.example.countrycity.dto.CityResponse;
import com.example.countrycity.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "City Controller", description = "Endpoints for managing and retrieving city details")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/cities/{cityId}")
    @Operation(summary = "Retrieve city details", description = "Fetches complete details of a city by its unique ID")
    public ResponseEntity<CityResponse> getCityById(
            @PathVariable @Parameter(description = "ID of the city to retrieve") Long cityId) {
        CityResponse city = cityService.getCityById(cityId);
        return ResponseEntity.ok(city);
    }

    @PutMapping("/countries/{countryId}/{cityName}")
    @Operation(summary = "Create city by name", description = "Adds a city to a country using country ID and city name (uses defaults for details)")
    public ResponseEntity<CityResponse> createCityByName(
            @PathVariable @Parameter(description = "ID of the country to add the city to") Long countryId,
            @PathVariable @Parameter(description = "Name of the city to create") String cityName) {
        CityResponse city = cityService.createCity(countryId, cityName);
        return ResponseEntity.status(HttpStatus.CREATED).body(city);
    }

    @PostMapping("/countries/{countryId}/cities")
    @Operation(summary = "Create city with details", description = "Adds a city to a country with full validation on population, zip code, and description")
    public ResponseEntity<CityResponse> createCityWithDetails(
            @PathVariable @Parameter(description = "ID of the country to add the city to") Long countryId,
            @Valid @RequestBody CityRequest cityRequest) {
        CityResponse city = cityService.createCityWithDetails(countryId, cityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(city);
    }
}
