package com.example.countrycity.controller;

import com.example.countrycity.dto.CityResponse;
import com.example.countrycity.dto.CountryResponse;
import com.example.countrycity.service.CityService;
import com.example.countrycity.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@Tag(name = "Country Controller", description = "Endpoints for managing countries and retrieval of associated cities")
public class CountryController {

    private final CountryService countryService;
    private final CityService cityService;

    public CountryController(CountryService countryService, CityService cityService) {
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all countries", description = "Fetches a flat list of all countries available in the database")
    public ResponseEntity<List<CountryResponse>> getAllCountries() {
        List<CountryResponse> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{countryId}/cities")
    @Operation(summary = "Retrieve cities for a country", description = "Fetches a paginated list of cities belonging to the specified country ID")
    public ResponseEntity<Page<CityResponse>> getCitiesByCountry(
            @PathVariable @Parameter(description = "ID of the country to fetch cities for") Long countryId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Zero-based page index") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Size of the page to retrieve") int size,
            @RequestParam(defaultValue = "name") @Parameter(description = "Column name to sort by (e.g. name, population)") String sortBy,
            @RequestParam(defaultValue = "asc") @Parameter(description = "Sort direction (asc or desc)") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CityResponse> cities = cityService.getCitiesByCountryId(countryId, pageable);
        return ResponseEntity.ok(cities);
    }

    @PutMapping("/{name}")
    @Operation(summary = "Create a country by name", description = "Adds a country to the database with the given name")
    public ResponseEntity<CountryResponse> createCountry(
            @PathVariable @Parameter(description = "Name of the country to add") String name) {
        CountryResponse createdCountry = countryService.createCountry(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
    }
}
